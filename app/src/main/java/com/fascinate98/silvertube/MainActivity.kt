package com.fascinate98.silvertube


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.fascinate98.silvertube.databinding.ActivityMainBinding
import com.fascinate98.silvertube.network.ListResponse
import com.fascinate98.silvertube.network.YoutubeApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    //private lateinit var mYoutubePlayer: YouTubePlayer
    private lateinit var bottomSheetDialog: BottomSheetDialog
    //private var videoIds: MutableList<String> = mutableListOf()
    private var playlistId: String = ""
    private var channelId: String = ""
    private var items: MutableList<ListResponse.Item> = mutableListOf()
    private lateinit var gson: Gson
    //private var num = 0
    lateinit var listAdapter: ListAdapter
    private lateinit var changeSetting: SettingYoutube



    init {
        instace = this
    }
    companion object{
        private var instace:MainActivity? = null
        fun getInstacne():MainActivity?{
            return instace
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeSetting = SettingYoutube.getInstance(this)
        changeSetting.setObject()
        gson = GsonBuilder().create()
        items.clear()
        sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE)
        val it = Intent(this, MyService::class.java)
        startService(it)

        if (sharedPreferences.getInt("isLogged", -1) == 1) {
            //youtube_player_view.visibility = View.VISIBLE;
            log_in.visibility = View.GONE;
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

        }
        var playliststr = sharedPreferences.getString("playlist", "")
        if(!playliststr.equals("")){
            var json = JSONArray(playliststr)
            for(i in 0 until json.length()){

                items.add(gson.fromJson(json.optString(i), ListResponse.Item::class.java))
            }

        }



        log_in.setOnClickListener {
            logIn()
        }

        listAdapter = ListAdapter(this)
        rv.adapter = listAdapter

        listAdapter.datas = items
        listAdapter.notifyDataSetChanged()
        val decoration = DividerItemDecoration(this, VERTICAL)
        rv.addItemDecoration(decoration)

        addplaylistbtn.setOnClickListener {
            playlistId = playlistidtxt.text.toString()
            var txt = playlistidtxt.text.toString()
            var result: String
            if (!txt.contains("list")) {
                var arr = txt.split("/")
                result = arr[arr.size - 1]
                channelId = result
                Log.d("ddgdgdgdg" , channelId)
                addAllChannelPlaylist()
            } else {
                result = txt.substring(txt.lastIndexOf("=") + 1)
                playlistId = result
                Log.d("fff" , playlistId)
                addPlaylist()
            }


            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            playlistidtxt.text.clear()
        }


        listAdapter.setOnItemClickListener(object : ListAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ListResponse.Item, pos: Int) {
                selectvideo(pos)
                //Toast.makeText(applicationContext, pos.toString(), Toast.LENGTH_SHORT).show()
            }


        })

        startbtnn.setOnClickListener {
            //mYoutubePlayer.play()
            //changeSetting.play()
            sharedPreferences.edit().remove("playlist").apply()
            sharedPreferences.edit().remove("num").apply()
            items.clear()
            listAdapter.notifyDataSetChanged()
        }

        endbtnn.setOnClickListener {
            //mYoutubePlayer.pause()
            changeSetting.pause()
        }

        nextbtnn.setOnClickListener {
            //playNextVideo()
            changeSetting.playNextVideo()
        }

        prevbtnn.setOnClickListener {
            //playPrevVideo()
            changeSetting.playPrevVideo()
        }

        volumnupbtnn.setOnClickListener {
            changeSetting.volumnUp()

        }

        volumndownbtnbtnn.setOnClickListener {
            changeSetting.volumnDown()
        }

    }

    fun deletevideo(position:Int){
        items.removeAt(position)
        saveInSp()
        Log.d("dssss", position.toString())
        listAdapter.notifyDataSetChanged()
    }

    fun selectvideo(position: Int){
        sharedPreferences.edit().putInt("num", position).apply()
        changeSetting.selectVideo()
    }

    private fun logIn() {
        bottomSheetDialog = BottomSheetDialog(this@MainActivity)
        val contentView = View.inflate(this@MainActivity, R.layout.activity_login_dialog, null)
        bottomSheetDialog.setContentView(contentView)
        val login_web: WebView = bottomSheetDialog.findViewById(R.id.login_webview)!!
        login_web.settings.javaScriptEnabled = true
        login_web.settings.domStorageEnabled = true
        login_web.settings.savePassword = true
        login_web.settings.saveFormData = true
        login_web.loadUrl("https://accounts.google.com/ServiceLogin?service=youtube&uilel=3&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Dm%26hl%3Dtr%26next%3Dhttps%253A%252F%252Fm.youtube.com%252F")
        login_web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (request.url.toString().startsWith("https://m.youtube.com")
                    || request.url.toString().startsWith("https://www.youtube.com")
                ) {
                    sharedPreferences.edit().putInt("isLogged", 1).apply()
                   // youtube_player_view.visibility = View.VISIBLE
                    log_in.visibility = View.GONE
                    startbtnn.visibility = View.VISIBLE
                    bottomSheetDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Logged in", Toast.LENGTH_SHORT).show()
                    return false
                }
                return false
            }
        }
        bottomSheetDialog.show()
    }


    private fun addPlaylist(){
        YoutubeApi.apiInstance().playlistItems("snippet, contentDetails", playlistId).enqueue(object: Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                Log.d(TAG, "onResponse: ${response.isSuccessful}")
                val result = response.body()!!.items

                Log.d(TAG, response.body().toString())
//                //sp에서 현재값 json -> string배열
//                val getShred = sharedPreferences.getString("playlist", "")
//                var oldvideolist = arrayListOf<String>()
//
//                var arrJson = JSONArray(getShred)
//                for(i in 0 until arrJson.length()){
//                    oldvideolist.add(arrJson.optString(i))
//                }

                //추가
                for(i in result) {
                    items.add(i)

                }

                //Toast.makeText(applicationContext, result[0].snippet.title, Toast.LENGTH_SHORT).show()
                saveInSp()
                listAdapter.notifyDataSetChanged()
                //다시 제이슨으로 변환
//                var jsonArr = JSONArray()
//                for(i in items){
//
//                    jsonArr.put(gson.toJson(i, ListResponse.Item::class.java))
//                }
//                var stringData = jsonArr.toString()
//                Log.d("dddddddddd" , stringData.toString())
//                Log.d("dd22dddddddd" , items.size.toString())
//                sharedPreferences.edit().putString("playlist", stringData).apply()
                //sharedPreferences.edit().putInt("num", items.size - 1).apply()

               // Toast.makeText(applicationContext, stringData + " " + oldvideolist.size , Toast.LENGTH_SHORT).show()


                //changeSetting.getYoutubePlayer().cueVideo(changeSetting.getVideoIdList()[changeSetting.getNum()],0f)
            }


            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveInSp(){
        var jsonArr = JSONArray()
        for(i in items){

            jsonArr.put(gson.toJson(i, ListResponse.Item::class.java))
        }
        var stringData = jsonArr.toString()
        Log.d("dddddddddd" , stringData.toString())
        Log.d("dd22dddddddd" , items.size.toString())
        sharedPreferences.edit().putString("playlist", stringData).apply()
        changeSetting.getListFromSp()
    }

    private fun addAllChannelPlaylist(){

        YoutubeApi.apiInstance().playList("snippet, id", channelId).enqueue(object: Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                Log.d(TAG, "onResponse: ${response.isSuccessful}")
                Log.d("dddfffffffffffffffffffffffff" , response.body().toString())
                val result = response.body()!!.items



                for(i in result) {
                    playlistId = i.id
                    addPlaylist()
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
