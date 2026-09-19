package com.islamicknowledge.platform.feature.prayer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

class PrayerAlarmReceiver : BroadcastReceiver() {
    companion object {
        private const val CHANNEL_ID = "prayer_times"
        private const val TOMORROW_OFFSET = 10
        private val names = mapOf(
            "fajr" to "ফজর", "dhuhr" to "যোহর", "asr" to "আসর",
            "maghrib" to "মাগরিব", "isha" to "এশা",
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prayerKey = intent.getStringExtra("prayer_key") ?: return
        val prayerName = names[prayerKey] ?: return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "নামাজের সময়", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "নামাজের ওয়াক্ত শুরু হলে বিজ্ঞপ্তি"
                }
            )
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            val contentIntent = launchIntent?.let {
                PendingIntent.getActivity(context, 4200, it, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("$prayerName এর সময় হয়েছে")
                .setContentText("নামাজের ওয়াক্ত শুরু হয়েছে।")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            if (contentIntent != null) builder.setContentIntent(contentIntent)
            manager.notify(4300 + names.keys.indexOf(prayerKey), builder.build())
        }

        scheduleTomorrow(context)
    }

    private fun scheduleTomorrow(context: Context) {
        val preferences = PrayerPreferences(context)
        val location = PrayerLocations.all.firstOrNull { it.id == preferences.getLocationId() }
            ?: PrayerLocations.default
        val tomorrowCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val tomorrow = PrayerCalculator.calculate(
            tomorrowCalendar.time, location, preferences.getMadhab(), preferences.getMethod()
        )
        PrayerNotificationScheduler.schedule(context, tomorrow, requestCodeOffset = TOMORROW_OFFSET)
    }
}
