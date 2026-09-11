package com.islamicknowledge.platform.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

private data class QuickAction(
    val title: String,
    val subtitle: String,
    val destination: HomeDestination,
)

enum class HomeDestination {
    QURAN,
    HADITH,
    PRAYER,
    DUA,
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onQuickActionClick: (HomeDestination) -> Unit = {},
) {
    val actions = listOf(
        QuickAction("কুরআন", "পড়া ও অনুসন্ধান", HomeDestination.QURAN),
        QuickAction("হাদিস", "সহিহ উৎসভিত্তিক জ্ঞান", HomeDestination.HADITH),
        QuickAction("নামাজ", "আজকের সময়সূচি", HomeDestination.PRAYER),
        QuickAction("দোয়া ও যিকর", "দৈনন্দিন আমল", HomeDestination.DUA),
    )

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("আসসালামু আলাইকুম", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Islamic Knowledge Platform",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Rounded.Schedule, contentDescription = null)
                        Text("পরবর্তী নামাজ", style = MaterialTheme.typography.titleMedium)
                    }
                    Text("ফজর", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "স্থান ও সময় নির্ধারণ করলে এখানে লাইভ কাউন্টডাউন দেখাবে।",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item { SectionHeader("দ্রুত অ্যাকশন") }

        items(actions) { action ->
            Card(
                onClick = { onQuickActionClick(action.destination) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val icon = when (action.title) {
                        "কুরআন" -> Icons.Rounded.MenuBook
                        "নামাজ" -> Icons.Rounded.Mosque
                        "দোয়া ও যিকর" -> Icons.Rounded.NightsStay
                        else -> Icons.Rounded.MenuBook
                    }
                    Icon(icon, contentDescription = null)
                    Column {
                        Text(action.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            action.subtitle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item { SectionHeader("আজকের কনটেন্ট") }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("দৈনিক আয়াত", style = MaterialTheme.typography.titleMedium)
                    Text("কনটেন্ট ইঞ্জিন যুক্ত হলে এখানে উৎসসহ আয়াত দেখানো হবে।")
                }
            }
        }
    }
}
