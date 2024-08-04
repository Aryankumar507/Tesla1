package com.example.tesla

import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivityNotificationBinding
import com.example.tesla.databinding.ActivityVideoBinding

class Notification : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.high.setOnClickListener {
            val notification = NotificationCompat.Builder(this,App().CHANNEL_ID1)
            notification.setContentTitle(binding.title.text.toString())
            notification.setContentText(binding.content.text.toString())
            notification.setPriority(NotificationCompat.PRIORITY_HIGH)
            notification.setSmallIcon(R.drawable.baseline_settings_24)
                .build()

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1,notification.build())
        }

        binding.low.setOnClickListener {

            val notification = NotificationCompat.Builder(this,App().CHANNEL_ID2)
            notification.setContentTitle(binding.title.toString())
            notification.setContentText(binding.content.toString())
            notification.setPriority(NotificationCompat.PRIORITY_DEFAULT)
            notification.setSmallIcon(R.drawable.baseline_settings_24)
                .build()

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(2,notification.build())
        }



    }
}