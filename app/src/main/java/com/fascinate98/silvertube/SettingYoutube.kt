package com.fascinate98.silvertube

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingYoutube() {

    companion object{
        private var instance: SettingYoutube? = null
        private lateinit var context: Context

        fun getInstance(_context: Context): SettingYoutube{
            return instance?: synchronized(this){
                instance ?: SettingYoutube().also {
                    context = _context
                    instance = it
                }
            }
        }
    }
    private lateinit var mYoutubePlayer: YouTubePlayer
    private lateinit var sharedPreferences: SharedPreferences
    private var videoIds: MutableList<String> = mutableListOf()
    private var playlistId: String = ""
    private var channelId: String = ""
    private var num = 0
    private lateinit var mAudioManager :AudioManager


    private lateinit var youTubePlayerView : YouTubePlayerView

    fun setObject(){
        mAudioManager = context.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
        youTubePlayerView =  YouTubePlayerView(context)
        youTubePlayerView.enableBackgroundPlayback(true)
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                setYoutubePlayer(youTubePlayer)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if(state == PlayerConstants.PlayerState.ENDED){
                    playNextVideo()
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                if (error == PlayerConstants.PlayerError.VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER) {
                    //logIn()
                }
            }
        })
        getListFromSp()

    }

    fun getListFromSp(){
        sharedPreferences = context.getSharedPreferences("log_check",
            AppCompatActivity.MODE_PRIVATE
        )
        val getShred = sharedPreferences.getString("playlist", "")
        var result = arrayListOf<String>()

        var arrJson = JSONArray(getShred)
        for(i in 0 until arrJson.length()){
            result.add(arrJson.optString(i))
        }
        videoIds = result

        val getnumsp = sharedPreferences.getInt("num", 0)
        num = getnumsp
    }

    fun setYoutubePlayer(_youtubePlayer: YouTubePlayer){
        mYoutubePlayer = _youtubePlayer
    }

    fun getYoutubePlayer(): YouTubePlayer{
        return mYoutubePlayer
    }

    fun getVideoIdList(): MutableList<String>{
        return videoIds
    }

    fun getNum(): Int {
        return num
    }


    fun play(){
        Toast.makeText(context, videoIds.size.toString(), Toast.LENGTH_SHORT).show()
        //mYoutubePlayer.play()
        getListFromSp()
        mYoutubePlayer.loadVideo(videoIds[num] , 0f)
    }

    fun pause(){
        mYoutubePlayer.pause()
    }

    fun addVideo(videoId: String){
        videoIds.add(videoId)
    }


    fun playNextVideo(){
        if(num < videoIds.size - 1){
            mYoutubePlayer.loadVideo(videoIds[++num] , 0f)
            sharedPreferences.edit().putInt("num", num).apply()
            play()
        }else{
            Toast.makeText( context, "마지막 영상", Toast.LENGTH_SHORT).show()
        }
    }

    fun playPrevVideo(){
        if(num >= 1){
            mYoutubePlayer.loadVideo(videoIds[--num] , 0f)
            sharedPreferences.edit().putInt("num", num).apply()
            play()
        }else{
            Toast.makeText( context, "첫번쨰 영상", Toast.LENGTH_SHORT).show()
        }
    }

    fun volumnUp(){
        mAudioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_RAISE,
            AudioManager.FLAG_SHOW_UI)
    }

    fun volumnDown(){
        mAudioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER,
            AudioManager.FLAG_SHOW_UI)
    }


}