package com.example.tesla

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App:Application() {

    final public val CHANNEL_ID1 = "CHANNEL_ID1"
    final public val CHANNEL_ID2 = "CHANNEL_ID2"
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            val Channel1 = NotificationChannel(CHANNEL_ID1,"Channel 1",NotificationManager.IMPORTANCE_HIGH)
            Channel1.description = "This is my High priority Channel for Notification"
            val Channel2 = NotificationChannel(CHANNEL_ID2,"Channel 2",NotificationManager.IMPORTANCE_DEFAULT)
            Channel2.description = "This is my Default priority Channel for Notification"

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(Channel1)
            manager.createNotificationChannel(Channel2)
        }
    }
}