package com.islamicknowledge.platform.feature.prayer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object PrayerNotificationScheduler {
    private const val BASE_REQUEST_CODE = 4100
    private const val ACTION_PRAYER = "com.islamicknowledge.platform.PRAYER_ALARM"

    fun schedule(context: Context, times: CalculatedPrayers) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val prayers = listOf(
            "fajr" to times.fajr,
            "dhuhr" to times.dhuhr,
            "asr" to times.asr,
            "maghrib" to times.maghrib,
            "isha" to times.isha,
        )
        prayers.forEachIndexed { index, (key, time) ->
            if (time.time <= System.currentTimeMillis()) return@forEachIndexed
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = ACTION_PRAYER
                putExtra("prayer_key", key)
            }
            val pending = PendingIntent.getBroadcast(
                context, BASE_REQUEST_CODE + index, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.time, pending)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.time, pending)
            } else {
                @Suppress("DEPRECATION")
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.time, pending)
            }
        }
    }
}
