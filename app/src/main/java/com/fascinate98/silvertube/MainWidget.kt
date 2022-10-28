package com.fascinate98.silvertube

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer


/**
 * Implementation of App Widget functionality.
 */
class MainWidget : AppWidgetProvider() {

    private val PLAY_BTN = "com.fascinate98.silvertube.PLAY_BTN"
    private val PAUSE_BTN = "com.fascinate98.silvertube.PAUSE_BTN"
    private val PREV_BTN = "com.fascinate98.silvertube.PREV_BTN"
    private val NEXT_BTN = "com.fascinate98.silvertube.NEXT_BTN"
    private val VUP_BTN = "com.fascinate98.silvertube.VUP_BTN"
    private val VDOWN_BTN = "com.fascinate98.silvertube.VDOWN_BTN"
    private lateinit var changeSetting: SettingYoutube


    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager , appWidgetId: Int ){
        val views = RemoteViews(context.packageName, R.layout.main_widget)

        val intent1 = Intent(context, MainWidget::class.java).setAction(PLAY_BTN)
        val pendingIntent1 =
            PendingIntent.getBroadcast(context, 0, intent1, 0)
        views.setOnClickPendingIntent(R.id.playbtn, pendingIntent1)


        val intent2 = Intent(context, MainWidget::class.java).setAction(PAUSE_BTN)
        val pendingIntent2 =
            PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.pausebtn, pendingIntent2)


        val intent3 = Intent(context, MainWidget::class.java).setAction(PREV_BTN)
        val pendingIntent3 =
            PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.prevbtn, pendingIntent3)


        val intent4 = Intent(context, MainWidget::class.java).setAction(NEXT_BTN)
        val pendingIntent4 =
            PendingIntent.getBroadcast(context, 0, intent4, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.nextbtn, pendingIntent4)


        val intent5 = Intent(context, MainWidget::class.java).setAction(VUP_BTN)
        val pendingIntent5 =
            PendingIntent.getBroadcast(context, 0, intent5, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.volumnupbtn, pendingIntent5)


        val intent6 = Intent(context, MainWidget::class.java).setAction(VDOWN_BTN)
        val pendingIntent6 =
            PendingIntent.getBroadcast(context, 0, intent6, PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.volumndownbtn, pendingIntent6)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            // Construct the RemoteViews object
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val views = RemoteViews(context!!.packageName, R.layout.main_widget)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context!!.packageName, MainWidget::class.java.getName())
        val appWidgets = appWidgetManager.getAppWidgetIds(thisAppWidget)
        var action = intent?.action
        changeSetting = SettingYoutube.getInstance(context)
        when(action){
            PLAY_BTN -> {
                changeSetting.play()
            }
            PAUSE_BTN -> {
                changeSetting.pause()
            }
            PREV_BTN -> {
                changeSetting.playPrevVideo()
            }
            NEXT_BTN -> {
                changeSetting.playNextVideo()
            }
            VUP_BTN -> {
                changeSetting.volumnUp()
            }
            VDOWN_BTN -> {
                changeSetting.volumnDown()
            }

        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        changeSetting = SettingYoutube.getInstance(context)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled

    }
}
