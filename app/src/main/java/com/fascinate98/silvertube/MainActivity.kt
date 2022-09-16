package com.fascinate98.silvertube


import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fascinate98.silvertube.databinding.ActivityMainBinding
import com.fascinate98.silvertube.network.ListResponse
import com.fascinate98.silvertube.network.YoutubeApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var mYoutubePlayer: YouTubePlayer
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var videoIds: MutableList<String> = mutableListOf()
    private var playlistId: String = ""
    private var channelId: String = ""
    private var num = 0
    private var volumn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAudioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
        initYouTubePlayerView()

        sharedPreferences = getSharedPreferences("log_check", MODE_PRIVATE);

        if (sharedPreferences.getInt("isLogged", -1) == 1) {
            youtube_player_view.visibility = View.VISIBLE;
            log_in.visibility = View.GONE;
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

        }

        log_in.setOnClickListener {
            logIn()
        }

        addplaylistbtn.setOnClickListener {
            playlistId = playlistidtxt.text.toString()
            addPlaylist()

        }

        addchannelbtn.setOnClickListener {
            channelId = channelidtxt.text.toString()
            addAllChannelPlaylist()
        }

        startbtnn.setOnClickListener {
            mYoutubePlayer.play()
        }

        endbtnn.setOnClickListener {
            mYoutubePlayer.pause()
        }

        nextbtnn.setOnClickListener {
           playNextVideo()
        }

        prevbtnn.setOnClickListener {
            playPrevVideo()
        }

        volumnupbtnn.setOnClickListener {

            mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_SHOW_UI)

        }

        volumndownbtnbtnn.setOnClickListener {

            mAudioManager.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_SHOW_UI)
        }
    }

    private fun playNextVideo(){
        if(num < videoIds.size - 1){
            mYoutubePlayer.loadVideo(videoIds[++num] , 0f)
        }else{
            Toast.makeText( this, "마지막 영상", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playPrevVideo(){
        if(num >= 1){
            mYoutubePlayer.loadVideo(videoIds[--num] , 0f)
        }else{
            Toast.makeText( this, "첫번쨰 영상", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initYouTubePlayerView() {

        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                mYoutubePlayer = youTubePlayer
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if(state == PlayerConstants.PlayerState.ENDED){
                    playNextVideo()
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerError) {
                super.onError(youTubePlayer, error)
                if (error == PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER) {
                    logIn()
                }
            }
        })

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
                    youtube_player_view.visibility = View.VISIBLE
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
        YoutubeApi.apiInstance().playlistItems("contentDetails", playlistId).enqueue(object: Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                Log.d(TAG, "onResponse: ${response.isSuccessful}")
                val result = response.body()!!.items
                for(i in result) {
                    videoIds.add(i.contentDetails.videoId)
                }
                mYoutubePlayer.cueVideo(videoIds[num],0f)
            }


            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addAllChannelPlaylist(){

        YoutubeApi.apiInstance().playList("id", channelId).enqueue(object: Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                Log.d(TAG, "onResponse: ${response.isSuccessful}")
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
