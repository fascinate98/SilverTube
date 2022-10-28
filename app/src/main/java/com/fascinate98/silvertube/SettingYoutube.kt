package com.fascinate98.silvertube

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fascinate98.silvertube.network.ListResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
        private lateinit var mYoutubePlayer: YouTubePlayer
        private lateinit var sharedPreferences: SharedPreferences
        private var items: MutableList<ListResponse.Item> = mutableListOf()
        private var videoIds: MutableList<String> = mutableListOf()
        private var playlistId: String = ""
        private var channelId: String = ""
        private var num = 0
        private var gson: Gson = GsonBuilder().create()
        private lateinit var mAudioManager :AudioManager
        private lateinit var youTubePlayerView : YouTubePlayerView

        fun getInstance(_context: Context): SettingYoutube{
            return instance?: synchronized(this){
                instance ?: SettingYoutube().also {
                    context = _context
                    instance = it
                }
            }
        }
    }





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
        items.clear()
        videoIds.clear()
        sharedPreferences = context.getSharedPreferences("shared",
            AppCompatActivity.MODE_PRIVATE
        )
        var playliststr = sharedPreferences.getString("playlist", "")
        if(!playliststr.equals("")) {
            var json = JSONArray(playliststr)
            for (i in 0 until json.length()) {
                var a = gson.fromJson(json.optString(i), ListResponse.Item::class.java)
                items.add(a)
                videoIds.add(a.contentDetails.videoId)

            }
        }

        val getnumsp = sharedPreferences.getInt("num", 0)
        Toast.makeText(context, getnumsp.toString(), Toast.LENGTH_SHORT).show()
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

        //mYoutubePlayer.play()
        //getListFromSp()
        Log.d("E", items.toString())
        Log.d("ddd", num.toString())
        //Toast.makeText(context, items.toString(), Toast.LENGTH_SHORT).show()
        mYoutubePlayer.loadVideo(videoIds[num] , 0f)
        mYoutubePlayer.play()
    }

    fun pause(){
        mYoutubePlayer.pause()
    }

    fun addVideo(videoId: String){
        videoIds.add(videoId)
    }

    fun selectVideo(){
        num = sharedPreferences.getInt("num", 0)
        Toast.makeText(context, num.toString(), Toast.LENGTH_SHORT).show()
        mYoutubePlayer.loadVideo(videoIds[num], 0f)
        mYoutubePlayer.play()
    }

    fun playNextVideo(){
        if(num <= videoIds.size - 1){
            mYoutubePlayer.loadVideo(videoIds[++num] , 0f)
            sharedPreferences.edit().putInt("num", num).apply()
            mYoutubePlayer.play()
        }else{
            Toast.makeText( context, "마지막 영상", Toast.LENGTH_SHORT).show()
        }
    }

    fun playPrevVideo(){
        if(num >= 1){
            mYoutubePlayer.loadVideo(videoIds[--num] , 0f)
            sharedPreferences.edit().putInt("num", num).apply()
            mYoutubePlayer.play()
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