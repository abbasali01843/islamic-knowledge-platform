package com.islamicknowledge.platform.feature.prayer

import java.util.Calendar
import java.util.Date
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.tan

enum class Madhab { HANAFI, STANDARD }
enum class CalcMethod { IFB, MWL, ISNA }

data class PrayerLocation(
    val id: String,
    val nameBengali: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: Double = 6.0,
    val zoneId: String = "Asia/Dhaka",
)

data class PrayerLabel(val key: String, val nameBengali: String, val nameArabic: String, val time: Date)

data class CalculatedPrayers(
    val fajr: Date,
    val sunrise: Date,
    val dhuhr: Date,
    val asr: Date,
    val maghrib: Date,
    val isha: Date,
    val sehriEnd: Date,
    val iftar: Date,
    val next: PrayerLabel,
    val current: PrayerLabel?,
    val currentProgressPercent: Int,
    val remainingMs: Long,
)

object PrayerLocations {
    val default = PrayerLocation("dhaka", "ঢাকা", 23.8103, 90.4125, 6.0, "Asia/Dhaka")
    val all = listOf(
        default,
        PrayerLocation("chattogram", "চট্টগ্রাম", 22.3569, 91.7832, 6.0, "Asia/Dhaka"),
        PrayerLocation("sylhet", "সিলেট", 24.8949, 91.8687, 6.0, "Asia/Dhaka"),
        PrayerLocation("rajshahi", "রাজশাহী", 24.3636, 88.6241, 6.0, "Asia/Dhaka"),
        PrayerLocation("khulna", "খুলনা", 22.8456, 89.5403, 6.0, "Asia/Dhaka"),
        PrayerLocation("barishal", "বরিশাল", 22.7010, 90.3535, 6.0, "Asia/Dhaka"),
        PrayerLocation("rangpur", "রংপুর", 25.7439, 89.2752, 6.0, "Asia/Dhaka"),
        PrayerLocation("mymensingh", "ময়মনসিংহ", 24.7471, 90.4203, 6.0, "Asia/Dhaka"),
        PrayerLocation("gazipur", "গাজীপুর", 23.9999, 90.4203, 6.0, "Asia/Dhaka"),
        PrayerLocation("cumilla", "কুমিল্লা", 23.4607, 91.1809, 6.0, "Asia/Dhaka"),
        PrayerLocation("coxs-bazar", "কক্সবাজার", 21.4272, 92.0058, 6.0, "Asia/Dhaka"),
    )
}

object PrayerCalculator {
    private fun d2r(d: Double) = d * Math.PI / 180.0
    private fun r2d(r: Double) = r * 180.0 / Math.PI
    private fun sinDeg(d: Double) = sin(d2r(d))
    private fun cosDeg(d: Double) = cos(d2r(d))
    private fun asinDeg(x: Double) = r2d(asin(max(-1.0, min(1.0, x))))
    private fun acosDeg(x: Double) = r2d(acos(max(-1.0, min(1.0, x))))
    private fun atan2Deg(y: Double, x: Double) = (r2d(atan2(y, x)) + 360.0) % 360.0
    private fun fixAngle(a: Double): Double { var r = a % 360.0; if (r < 0) r += 360.0; return r }
    private fun fixHour(h: Double): Double { var r = h % 24.0; if (r < 0) r += 24.0; return r }

    private fun julianDay(year: Int, month: Int, day: Int): Double {
        var y = year; var m = month
        if (m <= 2) { y -= 1; m += 12 }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun sunPosition(jd: Double): Pair<Double, Double> {
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sinDeg(g) + 0.02 * sinDeg(2 * g))
        val e = 23.439 - 0.00000036 * d
        val decl = asinDeg(sinDeg(e) * sinDeg(l))
        var ra = atan2Deg(cosDeg(e) * sinDeg(l), cosDeg(l)) / 15.0
        ra = fixHour(ra)
        val eq = (q / 15.0 - ra) * 60.0
        return decl to eq
    }

    private fun hourAngle(alt: Double, lat: Double, decl: Double): Double? {
        val cosH = (sinDeg(alt) - sinDeg(lat) * sinDeg(decl)) / (cosDeg(lat) * cosDeg(decl))
        if (cosH > 1 || cosH < -1) return null
        return acosDeg(cosH)
    }

    private fun hoursToDate(calDay: Calendar, decimalHours: Double): Date {
        val totalSeconds = Math.round(fixHour(decimalHours) * 3600).toInt()
        val h = totalSeconds / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        val c = calDay.clone() as Calendar
        c.set(Calendar.HOUR_OF_DAY, h)
        c.set(Calendar.MINUTE, m)
        c.set(Calendar.SECOND, s)
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    fun calculate(
        now: Date = Date(),
        location: PrayerLocation = PrayerLocations.default,
        madhab: Madhab = Madhab.HANAFI,
        method: CalcMethod = CalcMethod.IFB,
    ): CalculatedPrayers {
        val tz = java.util.TimeZone.getTimeZone(location.zoneId)
        val cal = Calendar.getInstance(tz).apply { time = now }
        val jd = julianDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
        val (decl, eq) = sunPosition(jd)
        val fajrAngle = when (method) {
            CalcMethod.IFB, CalcMethod.MWL -> 18.0
            CalcMethod.ISNA -> 15.0
        }
        val ishaAngle = when (method) {
            CalcMethod.IFB -> 18.0
            CalcMethod.MWL -> 17.0
            CalcMethod.ISNA -> 15.0
        }
        val timezoneHours = tz.getOffset(now.time) / 3600000.0
        val dhuhrDec = 12.0 + timezoneHours - location.longitude / 15.0 - eq / 60.0
        val hRise = hourAngle(-0.833, location.latitude, decl) ?: 90.0
        val sunriseDec = dhuhrDec - hRise / 15.0
        val sunsetDec = dhuhrDec + hRise / 15.0
        val hFajr = hourAngle(-fajrAngle, location.latitude, decl) ?: 108.0
        val fajrDec = dhuhrDec - hFajr / 15.0
        val shadow = if (madhab == Madhab.HANAFI) 2.0 else 1.0
        val asrAlt = r2d(atan(1.0 / (shadow + tan(kotlin.math.abs(d2r(location.latitude - decl))))))
        val hAsr = hourAngle(asrAlt, location.latitude, decl) ?: 60.0
        val asrDec = dhuhrDec + hAsr / 15.0
        val hIsha = hourAngle(-ishaAngle, location.latitude, decl) ?: 108.0
        val ishaDec = dhuhrDec + hIsha / 15.0

        val fajr = hoursToDate(cal, fajrDec)
        val sunrise = hoursToDate(cal, sunriseDec)
        val dhuhr = hoursToDate(cal, dhuhrDec)
        val asr = hoursToDate(cal, asrDec)
        val maghrib = hoursToDate(cal, sunsetDec)
        val isha = hoursToDate(cal, ishaDec)
        val sehriEnd = Date(fajr.time - 10 * 60 * 1000)

        val seq = listOf(
            PrayerLabel("fajr", "ফজর", "الفجر", fajr),
            PrayerLabel("sunrise", "সূর্যোদয়", "الشروق", sunrise),
            PrayerLabel("dhuhr", "যোহর", "الظهر", dhuhr),
            PrayerLabel("asr", "আসর", "العصر", asr),
            PrayerLabel("maghrib", "মাগরিব", "المغرب", maghrib),
            PrayerLabel("isha", "এশা", "العشاء", isha),
        )
        val nowMs = now.time
        var next = seq.firstOrNull { it.time.time > nowMs }
        if (next == null) {
            next = PrayerLabel("fajr", "ফজর", "الفجر", Date(fajr.time + 24 * 60 * 60 * 1000L))
        }
        var current: PrayerLabel? = null
        var progress = 0
        for (i in seq.indices.reversed()) {
            if (nowMs >= seq[i].time.time) {
                current = seq[i]
                val nextT = if (i < seq.lastIndex) seq[i + 1].time.time else fajr.time + 24 * 60 * 60 * 1000L
                val span = (nextT - seq[i].time.time).coerceAtLeast(1)
                progress = min(100, (((nowMs - seq[i].time.time).toDouble() / span) * 100).toInt())
                break
            }
        }
        return CalculatedPrayers(
            fajr, sunrise, dhuhr, asr, maghrib, isha, sehriEnd, maghrib,
            next, current, progress, max(0, next.time.time - nowMs),
        )
    }

    fun toBengaliDigits(value: String): String {
        val map = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        return value.map { if (it.isDigit()) map[it - '0'] else it }.joinToString("")
    }

    fun formatTimeBn(date: Date): String {
        val c = Calendar.getInstance().apply { time = date }
        var h = c.get(Calendar.HOUR_OF_DAY)
        val m = c.get(Calendar.MINUTE)
        val period = when {
            h in 3..5 -> "ভোর"
            h in 6..11 -> "সকাল"
            h in 12..14 -> "দুপুর"
            h in 15..17 -> "বিকাল"
            h in 18..19 -> "সন্ধ্যা"
            else -> "রাত"
        }
        h %= 12
        if (h == 0) h = 12
        return "${toBengaliDigits(h.toString().padStart(2, '0'))}:${toBengaliDigits(m.toString().padStart(2, '0'))} $period"
    }

    fun formatCountdownBn(ms: Long): String {
        val total = max(0, ms / 1000)
        val h = total / 3600
        val m = (total % 3600) / 60
        val s = total % 60
        return if (h > 0) "${toBengaliDigits(h.toString())} ঘণ্টা ${toBengaliDigits(m.toString())} মি ${toBengaliDigits(s.toString())} সে"
        else "${toBengaliDigits(m.toString())} মি ${toBengaliDigits(s.toString())} সে"
    }
}
