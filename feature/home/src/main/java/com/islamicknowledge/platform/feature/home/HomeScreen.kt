package com.islamicknowledge.platform.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    "আসসালামু আলাইকুম",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    "Islamic Knowledge Platform",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            "পরবর্তী নামাজ",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            "ফজর",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            "স্থান ও সময় নির্ধারণ করলে লাইভ কাউন্টডাউন দেখাবে।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                        )
                    }
                }
            }
        }

        item { SectionHeader("দ্রুত অ্যাকশন") }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                actions.chunked(2).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        rowActions.forEach { action ->
                            QuickActionCard(
                                action = action,
                                modifier = Modifier.weight(1f),
                                onClick = { onQuickActionClick(action.destination) },
                            )
                        }
                        if (rowActions.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        item { SectionHeader("আজকের কনটেন্ট") }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("দৈনিক আয়াত", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "কনটেন্ট ইঞ্জিন যুক্ত হলে এখানে উৎসসহ আয়াত দেখানো হবে।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val icon = when (action.destination) {
        HomeDestination.QURAN -> Icons.Rounded.MenuBook
        HomeDestination.HADITH -> Icons.Rounded.MenuBook
        HomeDestination.PRAYER -> Icons.Rounded.Mosque
        HomeDestination.DUA -> Icons.Rounded.NightsStay
    }

    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(26.dp),
            )
            Text(action.title, style = MaterialTheme.typography.titleMedium)
            Text(
                action.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
