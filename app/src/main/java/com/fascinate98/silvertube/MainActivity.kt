package com.fascinate98.silvertube

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.fascinate98.silvertube.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.NoSuchElementException


class MainActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener  {

    private lateinit var binding: ActivityMainBinding
    private val API_KEY = "AIzaSyB_uR2muYzt97ud4cbjoGYhE3sMnUWBkmw"
    private lateinit var PlayList_ID : String
    private lateinit var player: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //PlayList_ID = "PLWssa6qoBjgr0At1QzL5QqDZvTpqsPhca"
        getplaylistbtn.setOnClickListener {
            PlayList_ID = playlistidtxt.text.toString()
           // PlayList_ID = "PLWssa6qoBjgr0At1QzL5QqDZvTpqsPhca"
            youTubePlayerView.initialize(API_KEY, this)
        }

        startbtnn.setOnClickListener {
            player.play()
        }

        endbtnn.setOnClickListener {
            player.pause()
        }

        prevbtnn.setOnClickListener {
            try {
                player.previous()
            }catch (e: NoSuchElementException){
                Log.d("logggg", "last")
            }

        }
        nextbtnn.setOnClickListener {
            player.next()
        }

    }


    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        /** add listeners to YouTubePlayer instance **/
        p1?.setPlayerStateChangeListener(playerStateChangeListener);
        p1?.setPlaybackEventListener(playbackEventListener);
        /** Start buffering **/
        if (!p2) {
            p1?.cuePlaylist(PlayList_ID);
        }
        player = p1!!
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.d("ddddddd", "failllllll")
    }

    private val playbackEventListener: PlaybackEventListener = object : PlaybackEventListener {
        override fun onBuffering(arg0: Boolean) {}
        override fun onPaused() {}
        override fun onPlaying() {}
        override fun onSeekTo(arg0: Int) {}
        override fun onStopped() {}
    }

    private val playerStateChangeListener: PlayerStateChangeListener =
        object : PlayerStateChangeListener {
            override fun onAdStarted() {}
            override fun onError(arg0: YouTubePlayer.ErrorReason?) {}
            override fun onLoaded(arg0: String) {}
            override fun onLoading() {}
            override fun onVideoEnded() {}
            override fun onVideoStarted() {}
        }





}
