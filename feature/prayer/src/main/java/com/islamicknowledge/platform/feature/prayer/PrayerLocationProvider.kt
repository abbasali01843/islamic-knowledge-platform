package com.islamicknowledge.platform.feature.prayer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

object PrayerLocationProvider {
    fun hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(context: Context, onResult: (Double, Double) -> Unit) {
        if (!hasPermission(context)) return

        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER
        ).filter {
            runCatching { manager.isProviderEnabled(it) }.getOrDefault(false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val executor = Executors.newSingleThreadExecutor()
            val delivered = AtomicBoolean(false)
            providers.forEach { provider ->
                runCatching {
                    manager.getCurrentLocation(provider, null, executor) { location ->
                        if (location != null && delivered.compareAndSet(false, true)) {
                            onResult(location.latitude, location.longitude)
                            executor.shutdown()
                        }
                    }
                }
            }
            if (providers.isEmpty()) {
                executor.shutdown()
            }        } else {
            providers.asSequence()
                .mapNotNull {
                    provider ->
                    runCatching { manager.getLastKnownLocation(provider) }.getOrNull()
                }
                .maxByOrNull { it.time }
                ?.let { onResult(it.latitude, it.longitude) }
        }
    }
}
