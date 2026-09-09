package com.islamicknowledge.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.automirrored.rounded.MenuBook
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
import androidx.compose.ui.Modifier
import com.islamicknowledge.platform.core.design.IslamicKnowledgeTheme
import com.islamicknowledge.platform.feature.home.HomeScreen

private data class Destination(
    val label: String,
    val icon: @Composable () -> Unit
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
        Destination("হাদিস") { Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null) },
        Destination("খুঁজুন") { Icon(Icons.Rounded.Search, contentDescription = null) },
        Destination("আরও") { Icon(Icons.Rounded.MoreHoriz, contentDescription = null) }
    )
    var selected by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                destinations.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = { selected = index },
                        icon = destination.icon,
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selected) {
            0 -> HomeScreen(modifier = Modifier.fillMaxSize().then(Modifier))
            else -> PlaceholderScreen(destinations[selected].label)
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(title)
    }
}
