package com.islamicknowledge.platform.feature.prayer

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar
import java.util.Date

class PrayerAlarmPermissionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!manager.canScheduleExactAlarms()) return
        }
        val preferences = PrayerPreferences(context)
        val location = PrayerLocations.all.firstOrNull { it.id == preferences.getLocationId() }
            ?: PrayerLocations.default
        val today = PrayerCalculator.calculate(
            Date(), location, preferences.getMadhab(), preferences.getMethod()
        )
        PrayerNotificationScheduler.schedule(context, today)
        val tomorrowDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time
        val tomorrow = PrayerCalculator.calculate(
            tomorrowDate, location, preferences.getMadhab(), preferences.getMethod()
        )
        PrayerNotificationScheduler.schedule(context, tomorrow, requestCodeOffset = 10)
    }
}
