package com.islamicknowledge.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

private enum class AppTab(
    val label: String,
    val index: Int,
) {
    HOME("হোম", 0),
    QURAN("কুরআন", 1),
    HADITH("হাদিস", 2),
    SEARCH("খুঁজুন", 3),
    MORE("আরও", 4),
}

private data class Destination(
    val tab: AppTab,
    val icon: @Composable () -> Unit,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        Destination(AppTab.HOME) { Icon(Icons.Rounded.Home, contentDescription = null) },
        Destination(AppTab.QURAN) { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination(AppTab.HADITH) { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination(AppTab.SEARCH) { Icon(Icons.Rounded.Search, contentDescription = null) },
        Destination(AppTab.MORE) { Icon(Icons.Rounded.MoreHoriz, contentDescription = null) },
    )
    var selected by rememberSaveable { mutableIntStateOf(AppTab.HOME.index) }
    var selectedSurah by remember { mutableStateOf<Surah?>(null) }
    var selectedAyah by remember { mutableStateOf<Int?>(null) }

    fun openQuran() {
        selected = AppTab.QURAN.index
        selectedSurah = null
        selectedAyah = null
    }

    fun openTab(tab: AppTab) {
        selected = tab.index
        selectedSurah = null
        selectedAyah = null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                destinations.forEach { destination ->
                    NavigationBarItem(
                        selected = selected == destination.tab.index,
                        onClick = { openTab(destination.tab) },
                        icon = destination.icon,
                        label = { Text(destination.tab.label) },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selected) {
                AppTab.HOME.index -> HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    onQuickActionClick = { destination ->
                        when (destination) {
                            HomeDestination.QURAN -> openQuran()
                            HomeDestination.HADITH -> openTab(AppTab.HADITH)
                            HomeDestination.PRAYER,
                            HomeDestination.DUA -> openTab(AppTab.MORE)
                        }
                    },
                )

                AppTab.QURAN.index -> {
                    val surah = selectedSurah
                    if (surah == null) {
                        QuranScreen(onSurahClick = { clickedSurah, ayah ->
                            selectedSurah = clickedSurah
                            selectedAyah = ayah
                        })
                    } else {
                        QuranReaderScreen(
                            surah = surah,
                            initialAyah = selectedAyah,
                            onBack = {
                                selectedSurah = null
                                selectedAyah = null
                            },
                        )
                    }
                }

                AppTab.SEARCH.index -> QuranSearchScreen(onResultClick = { clickedSurah, ayah ->
                    selectedSurah = clickedSurah
                    selectedAyah = ayah
                    selected = AppTab.QURAN.index
                })

                AppTab.HADITH.index -> PlaceholderScreen("হাদিস")
                AppTab.MORE.index -> PlaceholderScreen("আরও")
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
