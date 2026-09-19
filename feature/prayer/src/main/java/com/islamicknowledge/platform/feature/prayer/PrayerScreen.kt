package com.islamicknowledge.platform.feature.prayer

import android.Manifest
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import kotlinx.coroutines.delay
import java.util.Date

class PrayerPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("prayer_prefs", Context.MODE_PRIVATE)
    fun getLocationId(): String = prefs.getString("location_id", PrayerLocations.default.id) ?: PrayerLocations.default.id
    fun setLocationId(id: String) = prefs.edit().putString("location_id", id).apply()
    fun getMadhab(): Madhab = if (prefs.getString("madhab", "HANAFI") == "STANDARD") Madhab.STANDARD else Madhab.HANAFI
    fun setMadhab(m: Madhab) = prefs.edit().putString("madhab", m.name).apply()
    fun getMethod(): CalcMethod = when (prefs.getString("method", "IFB")) {
        "MWL" -> CalcMethod.MWL
        "ISNA" -> CalcMethod.ISNA
        else -> CalcMethod.IFB
    }
    fun setMethod(m: CalcMethod) = prefs.edit().putString("method", m.name).apply()
    fun getGpsLocation(): PrayerLocation? {
        val lat = prefs.getString("gps_lat", null)?.toDoubleOrNull() ?: return null
        val lon = prefs.getString("gps_lon", null)?.toDoubleOrNull() ?: return null
        val name = prefs.getString("gps_name", "আমার অবস্থান") ?: "আমার অবস্থান"
        return PrayerLocation("gps", name, lat, lon, 6.0)
    }
    fun setGpsLocation(latitude: Double, longitude: Double) {
        prefs.edit().putString("gps_lat", latitude.toString()).putString("gps_lon", longitude.toString()).putString("gps_name", "আমার অবস্থান").apply()
    }
}

@Composable
fun PrayerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val preferences = remember(context) { PrayerPreferences(context) }
    var locationId by remember { mutableStateOf(preferences.getLocationId()) }
    var madhab by remember { mutableStateOf(preferences.getMadhab()) }
    var method by remember { mutableStateOf(preferences.getMethod()) }
    var now by remember { mutableStateOf(Date()) }
    var pickLocation by remember { mutableStateOf(false) }
    var gpsLocation by remember { mutableStateOf(preferences.getGpsLocation()) }
    val location = gpsLocation ?: (PrayerLocations.all.firstOrNull { it.id == locationId } ?: PrayerLocations.default)
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            PrayerLocationProvider.requestCurrentLocation(context) { latitude, longitude ->
                preferences.setGpsLocation(latitude, longitude)
                gpsLocation = preferences.getGpsLocation()
            }
        }
    }
    val times = remember(now, location, madhab, method) {
        PrayerCalculator.calculate(now, location, madhab, method)
    }
    LaunchedEffect(location, madhab, method, times.fajr) {
        PrayerNotificationScheduler.schedule(context, times)
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = Date()
        }
    }
    val rows = listOf(
        "ফজর" to times.fajr,
        "সূর্যোদয়" to times.sunrise,
        "যোহর" to times.dhuhr,
        "আসর" to times.asr,
        "মাগরিব" to times.maghrib,
        "এশা" to times.isha,
    )
    Column(modifier = modifier.fillMaxSize()) {
        SectionHeader(title = "নামাজ", modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
        if (pickLocation) {
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PrayerLocations.all, key = { it.id }) { loc ->
                    Card(modifier = Modifier.fillMaxWidth().clickable {
                        locationId = loc.id
                        preferences.setLocationId(loc.id)
                        pickLocation = false
                    }) {
                        Text(loc.nameBengali, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            return
        }
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = gpsLocation != null,
                        onClick = {
                            if (PrayerLocationProvider.hasPermission(context)) {
                                PrayerLocationProvider.requestCurrentLocation(context) { latitude, longitude ->
                                    preferences.setGpsLocation(latitude, longitude)
                                    gpsLocation = preferences.getGpsLocation()
                                }
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                                )
                            }
                        },
                        label = { Text(if (gpsLocation != null) "আমার অবস্থান" else location.nameBengali) },
                        leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null) },
                    )
                    FilterChip(
                        selected = madhab == Madhab.HANAFI,
                        onClick = { madhab = Madhab.HANAFI; preferences.setMadhab(Madhab.HANAFI) },
                        label = { Text("হানাফী") },
                    )
                    FilterChip(
                        selected = madhab == Madhab.STANDARD,
                        onClick = { madhab = Madhab.STANDARD; preferences.setMadhab(Madhab.STANDARD) },
                        label = { Text("শাফেয়ী") },
                    )
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = method == CalcMethod.IFB,
                        onClick = { method = CalcMethod.IFB; preferences.setMethod(CalcMethod.IFB) },
                        label = { Text("বাংলাদেশ ১৮°") },
                    )
                    FilterChip(
                        selected = method == CalcMethod.MWL,
                        onClick = { method = CalcMethod.MWL; preferences.setMethod(CalcMethod.MWL) },
                        label = { Text("MWL") },
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("পরবর্তী ওয়াক্ত", style = MaterialTheme.typography.labelLarge)
                        Text(times.next.nameBengali, style = MaterialTheme.typography.headlineMedium)
                        Text(PrayerCalculator.formatTimeBn(times.next.time), style = MaterialTheme.typography.titleLarge)
                        Text("বাকি: ${PrayerCalculator.formatCountdownBn(times.remainingMs)}", style = MaterialTheme.typography.bodyMedium)
                        if (times.current != null) {
                            Text(
                                "চলতি: ${times.current.nameBengali} (${PrayerCalculator.toBengaliDigits(times.currentProgressPercent.toString())}%)",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            LinearProgressIndicator(
                                progress = { times.currentProgressPercent / 100f },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                            )
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Card(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("সেহরি শেষ", style = MaterialTheme.typography.labelMedium)
                            Text(PrayerCalculator.formatTimeBn(times.sehriEnd), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Card(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("ইফতার", style = MaterialTheme.typography.labelMedium)
                            Text(PrayerCalculator.formatTimeBn(times.iftar), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
            item {
                if (!PrayerAlarmSettings.canScheduleExact(context)) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("সময়মতো নামাজের নোটিফিকেশন", style = MaterialTheme.typography.titleMedium)
                            Text("নির্ভুল সময়ে নোটিফিকেশন পেতে “Alarms & reminders” অনুমতি দিন। অনুমতি না থাকলেও সাধারণ নোটিফিকেশন চালু থাকবে।", style = MaterialTheme.typography.bodyMedium)
                            Button(onClick = { PrayerAlarmSettings.openExactAlarmSettings(context) }) {
                                Text("অনুমতি দিন")
                            }
                        }
                    }
                }
            }
            item { Text("আজকের সময়সূচি", style = MaterialTheme.typography.titleMedium) }
            items(rows) { (title, time) ->
                val isNext = times.next.nameBengali == title
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (isNext) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    else CardDefaults.cardColors(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Rounded.Schedule, contentDescription = null)
                            Text(title, style = MaterialTheme.typography.titleMedium)
                        }
                        Text(PrayerCalculator.formatTimeBn(time), style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
