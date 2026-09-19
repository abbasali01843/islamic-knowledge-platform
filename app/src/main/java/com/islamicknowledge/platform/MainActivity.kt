package com.islamicknowledge.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.islamicknowledge.platform.core.design.IslamicKnowledgeTheme
import com.islamicknowledge.platform.core.model.quran.Surah
import com.islamicknowledge.platform.feature.home.HomeDestination
import com.islamicknowledge.platform.feature.hadith.HadithScreen
import com.islamicknowledge.platform.feature.calendar.CalendarScreen
import com.islamicknowledge.platform.feature.zakat.ZakatScreen
import com.islamicknowledge.platform.feature.learn.LearnSalahScreen
import com.islamicknowledge.platform.feature.qibla.QiblaScreen
import com.islamicknowledge.platform.feature.dua.DuaScreen
import com.islamicknowledge.platform.feature.home.HomeScreen
import com.islamicknowledge.platform.feature.prayer.PrayerScreen
import com.islamicknowledge.platform.feature.quran.QuranReaderScreen
import com.islamicknowledge.platform.feature.quran.QuranScreen
import com.islamicknowledge.platform.feature.quran.QuranSearchScreen
import com.islamicknowledge.platform.feature.quran.findQuranSurah

private data class Destination(val label: String, val icon: @Composable () -> Unit)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }
        setContent {
            IslamicKnowledgeTheme {
                IslamicKnowledgeApp(
                    requestNotificationPermission = {
                        if (android.os.Build.VERSION.SDK_INT >= 33) {
                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun IslamicKnowledgeApp(requestNotificationPermission: () -> Unit) {
    val destinations = listOf(
        Destination("হোম") { Icon(Icons.Rounded.Home, contentDescription = null) },
        Destination("কুরআন") { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination("নামাজ") { Icon(Icons.Rounded.Mosque, contentDescription = null) },
        Destination("খুঁজুন") { Icon(Icons.Rounded.Search, contentDescription = null) },
        Destination("আরও") { Icon(Icons.Rounded.MoreHoriz, contentDescription = null) },
    )
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var moreRoute by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(selected) {
        if (selected == 2) requestNotificationPermission()
    }
    var selectedSurahNumber by rememberSaveable { mutableIntStateOf(0) }
    var selectedAyah by rememberSaveable { mutableIntStateOf(0) }
    val selectedSurah = findQuranSurah(selectedSurahNumber)
    val isReaderOpen = selected == 1 && selectedSurah != null

    fun openQuran() { selected = 1; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openPrayer() { selected = 2; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openHadith() { selected = 4; moreRoute = 1; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openDua() { selected = 4; moreRoute = 2; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openQibla() { selected = 4; moreRoute = 3; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openCalendar() { selected = 4; moreRoute = 4; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openZakat() { selected = 4; moreRoute = 5; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openLearn() { selected = 4; moreRoute = 6; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openPlaceholder(index: Int) { selected = index; selectedSurahNumber = 0; selectedAyah = 0 }
    fun openSurah(surah: Surah, ayah: Int?) {
        selected = 1
        selectedSurahNumber = surah.number
        selectedAyah = ayah ?: 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isReaderOpen) NavigationBar {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = {
                            selected = index
                            if (index != 1) {
                                selectedSurahNumber = 0
                                selectedAyah = 0
                            }
                        },
                        icon = destination.icon,
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().then(
                if (isReaderOpen) Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                else Modifier.padding(paddingValues),
            ),
        ) {
            when (selected) {
                0 -> HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onQuickActionClick = { destination ->
                        when (destination) {
                            HomeDestination.QURAN -> openQuran()
                            HomeDestination.PRAYER -> openPrayer()
                            HomeDestination.HADITH -> openHadith()
                            HomeDestination.DUA -> openDua()
                        }
                    },
                )
                1 -> {
                    val surah = selectedSurah
                    if (surah == null) {
                        QuranScreen(onSurahClick = ::openSurah)
                    } else {
                        QuranReaderScreen(
                            surah = surah,
                            initialAyah = selectedAyah.takeIf { it > 0 },
                            onBack = { selectedSurahNumber = 0; selectedAyah = 0 },
                            onNavigateToSurah = { next ->
                                selectedSurahNumber = next.number
                                selectedAyah = 0
                            },
                        )
                    }
                }
                2 -> PrayerScreen(modifier = Modifier.fillMaxSize())
                3 -> QuranSearchScreen(onResultClick = ::openSurah)
                4 -> when (moreRoute) { 1 -> HadithScreen(modifier = Modifier.fillMaxSize()); 2 -> DuaScreen(modifier = Modifier.fillMaxSize()); 3 -> QiblaScreen(modifier = Modifier.fillMaxSize()); 4 -> CalendarScreen(modifier = Modifier.fillMaxSize()); 5 -> ZakatScreen(modifier = Modifier.fillMaxSize()); 6 -> LearnSalahScreen(modifier = Modifier.fillMaxSize()); else -> PlaceholderScreen("আরও") }
                else -> PlaceholderScreen(destinations[selected].label)
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(title) }
}
