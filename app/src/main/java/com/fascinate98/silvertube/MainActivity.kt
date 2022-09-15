package com.fascinate98.silvertube


import android.R
import android.os.Bundle
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import androidx.appcompat.app.AppCompatActivity
import com.fascinate98.silvertube.databinding.ActivityMainBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding
    private val API_KEY = "AIzaSyB_uR2muYzt97ud4cbjoGYhE3sMnUWBkmw"
    private lateinit var token :String
    private lateinit var PlayList_ID : String
    private lateinit var player: YouTubePlayer

    private val PLAYLIST_ID = "PLWssa6qoBjgoyyRvWThbGl0w7zJ_qz7km"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycle.addObserver(youtube_player_view);

        initYouTubePlayerView();

    }

    private fun initYouTubePlayerView(){
       var iFramePlayerOptions = IFramePlayerOptions.Builder()
           .controls(1)
           .listType("playlist")
           .list(PLAYLIST_ID)
           .build();

        lifecycle.addObserver(youtube_player_view)
        youtube_player_view.enableAutomaticInitialization = false
        youtube_player_view.enableBackgroundPlayback(true)
        youtube_player_view.initialize(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    setPlayNextVideoButtonClickListener(youTubePlayer)

                } },
            true,
            iFramePlayerOptions
        )

    }


    private fun setPlayNextVideoButtonClickListener(youTubePlayer: YouTubePlayer) {
        nextbtnn.setOnClickListener {

        }
    }


//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        // Checks the orientation of the screen
////        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
////            youTubePlayerView.enterFullScreen()
////        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
////            youTubePlayerView.exitFullScreen()
////        }
//    }
}

//    inner class ProfileTask : AsyncTask<String?, Void, JSONObject>() {
//        override fun doInBackground(vararg tokens: String?): JSONObject? {
//            val client = OkHttpClient()
//            val builtUri =
//                Uri.parse("https://www.googleapis.com/youtube/v3/playlistItems?").buildUpon()
//                    .appendQueryParameter("part", "snippet")
//                    .appendQueryParameter("mine", "true")
//                    .appendQueryParameter("access_token", tokens[0])
//                    .appendQueryParameter("playlistId", "PLWssa6qoBjgrLFyD7dwVuKlhVtM_SzW5_")
//                    .build()
//
//            val request = Request.Builder()
//                .url(builtUri.toString())
//                .build()
//            try {
//                val response = client.newCall(request).execute()
//                val jsonBody: String = response.body()!!.string()
//                Log.i(
//                    "LOG_TAG",
//                    String.format("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv %s", builtUri.toString())
//                )
//                Log.i(
//                    "LOG_TAG",
//                    String.format("vvvvvvvvvdfffffffffffffffffdvvvv %s", jsonBody.toString())
//                )
//                return JSONObject(jsonBody)
//            } catch (exception: Exception) {
//                Log.w("LOG_TAG", exception)
//            }
//            return null
//        }
//    }



