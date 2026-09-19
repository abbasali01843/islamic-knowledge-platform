package com.islamicknowledge.platform.feature.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private const val KAABA_LAT = 21.422487
private const val KAABA_LON = 39.826206

private fun qiblaBearing(lat: Double, lon: Double): Double {
    val p1 = Math.toRadians(lat)
    val p2 = Math.toRadians(KAABA_LAT)
    val dl = Math.toRadians(KAABA_LON - lon)
    return (Math.toDegrees(atan2(sin(dl) * cos(p2), cos(p1) * sin(p2) - sin(p1) * cos(p2) * cos(dl))) + 360.0) % 360.0
}

@Composable
fun QiblaScreen(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember(context) { context.getSharedPreferences("prayer_prefs", Context.MODE_PRIVATE) }
    val lat = prefs.getString("gps_lat", null)?.toDoubleOrNull()
    val lon = prefs.getString("gps_lon", null)?.toDoubleOrNull()
    val bearing = remember(lat, lon) { if (lat != null && lon != null) qiblaBearing(lat, lon) else Double.NaN }
    var heading by remember { mutableDoubleStateOf(Double.NaN) }

    DisposableEffect(Unit) {
        val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val rotation = context.getSystemService(WindowManager::class.java)?.defaultDisplay?.rotation ?: Surface.ROTATION_0
                val rawMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rawMatrix, event.values)
                val adjustedMatrix = FloatArray(9)
                when (rotation) {
                    Surface.ROTATION_90 -> SensorManager.remapCoordinateSystem(rawMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, adjustedMatrix)
                    Surface.ROTATION_180 -> SensorManager.remapCoordinateSystem(rawMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, adjustedMatrix)
                    Surface.ROTATION_270 -> SensorManager.remapCoordinateSystem(rawMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, adjustedMatrix)
                    else -> rawMatrix.copyInto(adjustedMatrix)
                }
                val orientation = FloatArray(3)
                SensorManager.getOrientation(adjustedMatrix, orientation)
                heading = (Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (sensor != null) manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose { manager.unregisterListener(listener) }
    }

    Column(modifier.fillMaxSize()) {
        SectionHeader(title = "কিবলা", modifier = Modifier.padding(16.dp))
        Card(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (bearing.isNaN()) {
                    Text("নামাজ পেজে GPS অবস্থান চালু করুন।")
                } else {
                    Text("কাবার দিক: " + bearing.roundToInt() + "°", style = MaterialTheme.typography.headlineSmall)
                    if (heading.isNaN()) {
                        Text("এই ডিভাইসে Rotation Vector sensor পাওয়া যায়নি।")
                    } else {
                        val delta = ((bearing - heading + 540.0) % 360.0) - 180.0
                        Text("বর্তমান দিক: " + heading.roundToInt() + "°")
                        Text(if (abs(delta) < 5.0) "✓ আপনি কিবলার দিকে আছেন" else "কিবলার দিকে " + abs(delta).roundToInt() + "° ঘুরুন")
                    }
                }
            }
        }
    }
}