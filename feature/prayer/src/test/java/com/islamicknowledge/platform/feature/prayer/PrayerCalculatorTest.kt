package com.islamicknowledge.platform.feature.prayer

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.Date

class PrayerCalculatorTest {
    private fun date(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date =
        Calendar.getInstance().apply {
            set(year, month - 1, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

    @Test
    fun bangladeshPrayerTimesAreChronologicallyOrdered() {
        val result = PrayerCalculator.calculate(
            now = date(2026, 9, 19, 12, 0),
            location = PrayerLocations.default,
            madhab = Madhab.HANAFI,
            method = CalcMethod.IFB,
        )

        assertTrue(result.fajr.before(result.sunrise))
        assertTrue(result.sunrise.before(result.dhuhr))
        assertTrue(result.dhuhr.before(result.asr))
        assertTrue(result.asr.before(result.maghrib))
        assertTrue(result.maghrib.before(result.isha))
        assertTrue(result.sehriEnd.before(result.fajr))
        assertTrue(result.iftar == result.maghrib)
    }

    @Test
    fun hanafiAsrIsLaterThanStandardAsr() {
        val now = date(2026, 9, 19, 12, 0)
        val hanafi = PrayerCalculator.calculate(
            now = now,
            location = PrayerLocations.default,
            madhab = Madhab.HANAFI,
            method = CalcMethod.IFB,
        )
        val standard = PrayerCalculator.calculate(
            now = now,
            location = PrayerLocations.default,
            madhab = Madhab.STANDARD,
            method = CalcMethod.IFB,
        )

        assertTrue(hanafi.asr.after(standard.asr))
    }
}
