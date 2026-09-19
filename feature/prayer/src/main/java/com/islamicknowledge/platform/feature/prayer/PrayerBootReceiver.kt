package com.islamicknowledge.platform.feature.prayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.util.Date

class PrayerBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) return

        val preferences = PrayerPreferences(context)
        val location = PrayerLocations.all.firstOrNull { it.id == preferences.getLocationId() }
            ?: PrayerLocations.default
        val now = Date()
        val today = PrayerCalculator.calculate(now, location, preferences.getMadhab(), preferences.getMethod())
        PrayerNotificationScheduler.schedule(context, today)

        val tomorrowCalendar = Calendar.getInstance().apply {
            time = now
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val tomorrow = PrayerCalculator.calculate(
            tomorrowCalendar.time, location, preferences.getMadhab(), preferences.getMethod()
        )
        PrayerNotificationScheduler.schedule(context, tomorrow, requestCodeOffset = 10)
    }
}
