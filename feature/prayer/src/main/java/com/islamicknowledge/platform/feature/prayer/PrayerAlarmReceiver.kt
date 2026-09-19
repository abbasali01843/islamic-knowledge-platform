package com.islamicknowledge.platform.feature.prayer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val key = intent.getStringExtra("prayer_key") ?: return
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel("prayer_times", "নামাজের ওয়াক্ত", NotificationManager.IMPORTANCE_HIGH)
            )
        }
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return

        val names = mapOf(
            "fajr" to "ফজর",
            "dhuhr" to "যোহর",
            "asr" to "আসর",
            "maghrib" to "মাগরিব",
            "isha" to "এশা",
        )
        val name = names[key] ?: key
        val notification = NotificationCompat.Builder(context, "prayer_times")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("নামাজের ওয়াক্ত")
            .setContentText("$name এর সময় হয়েছে")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(key.hashCode(), notification)
    }
}
