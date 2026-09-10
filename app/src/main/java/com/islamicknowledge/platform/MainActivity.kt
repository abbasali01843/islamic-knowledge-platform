package com.islamicknowledge.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
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
import com.islamicknowledge.platform.feature.home.HomeScreen
import com.islamicknowledge.platform.feature.quran.QuranReaderScreen
import com.islamicknowledge.platform.feature.quran.QuranScreen

private data class Destination(
    val label: String,
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
        Destination("হোম") { Icon(Icons.Rounded.Home, contentDescription = null) },
        Destination("কুরআন") { Icon(Icons.Rounded.MenuBook, contentDescription = null) },
        Destination("হাদিস") { Icon(Icons.Rounded.MenuBook, contentDescription = null) },
        Destination("খুঁজুন") { Icon(Icons.Rounded.Search, contentDescription = null) },
        Destination("আরও") { Icon(Icons.Rounded.MoreHoriz, contentDescription = null) },
    )
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var selectedSurah by remember { mutableStateOf<Surah?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = {
                            selected = index
                            if (index != 1) selectedSurah = null
                        },
                        icon = destination.icon,
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selected) {
                0 -> HomeScreen(modifier = Modifier.fillMaxSize())
                1 -> {
                    val surah = selectedSurah
                    if (surah == null) {
                        QuranScreen(onSurahClick = { selectedSurah = it })
                    } else {
                        QuranReaderScreen(
                            surah = surah,
                            onBack = { selectedSurah = null },
                        )
                    }
                }
                else -> PlaceholderScreen(destinations[selected].label)
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(title)
    }
}
