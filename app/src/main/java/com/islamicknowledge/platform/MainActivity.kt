package com.islamicknowledge.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.islamicknowledge.platform.core.design.IslamicKnowledgeTheme
import com.islamicknowledge.platform.core.model.quran.Surah
import com.islamicknowledge.platform.feature.home.HomeDestination
import com.islamicknowledge.platform.feature.home.HomeScreen
import com.islamicknowledge.platform.feature.quran.QuranReaderScreen
import com.islamicknowledge.platform.feature.quran.QuranScreen
import com.islamicknowledge.platform.feature.quran.QuranSearchScreen

private data class Destination(
    val label: String,
    val icon: @Composable () -> Unit,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IslamicKnowledgeTheme {
                IslamicKnowledgeApp()
            }
        }
    }
}

@Composable
private fun IslamicKnowledgeApp() {
    val destinations = listOf(
        Destination("হোম") { Icon(Icons.Rounded.Home, contentDescription = null) },
        Destination("কুরআন") { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination("হাদিস") { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination("খুঁজুন") { Icon(Icons.Rounded.Search, contentDescription = null) },
        Destination("আরও") { Icon(Icons.Rounded.MoreHoriz, contentDescription = null) },
    )

    var selected by rememberSaveable { mutableIntStateOf(0) }
    var selectedSurahNumber by rememberSaveable { mutableIntStateOf(0) }
    var selectedAyah by rememberSaveable { mutableIntStateOf(0) }
    val selectedSurah = quranSurahs.firstOrNull { it.number == selectedSurahNumber }
    val isReaderOpen = selected == 1 && selectedSurah != null

    fun openQuran() {
        selected = 1
        selectedSurahNumber = 0
        selectedAyah = 0
    }

    fun openPlaceholder(index: Int) {
        selected = index
        selectedSurahNumber = 0
        selectedAyah = 0
    }

    fun openSurah(surah: Surah, ayah: Int?) {
        selected = 1
        selectedSurahNumber = surah.number
        selectedAyah = ayah ?: 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!isReaderOpen) {
                NavigationBar {
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
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isReaderOpen) {
                        Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                    } else {
                        Modifier.padding(paddingValues)
                    },
                ),
        ) {
            when (selected) {
                0 -> HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onQuickActionClick = { destination ->
                        when (destination) {
                            HomeDestination.QURAN -> openQuran()
                            HomeDestination.HADITH -> openPlaceholder(2)
                            HomeDestination.PRAYER,
                            HomeDestination.DUA -> openPlaceholder(4)
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
                            onBack = {
                                selectedSurahNumber = 0
                                selectedAyah = 0
                            },
                        )
                    }
                }
                3 -> QuranSearchScreen(onResultClick = ::openSurah)
                else -> PlaceholderScreen(destinations[selected].label)
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title)
    }
}
