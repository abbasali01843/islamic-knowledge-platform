package com.islamicknowledge.platform.feature.hajj

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

private val steps = listOf(
    "ইহরাম ও নিয়ত",
    "তালবিয়া",
    "মক্কায় পৌঁছে তাওয়াফ",
    "সাফা-মারওয়া সাঈ",
    "মিনা",
    "আরাফাহ",
    "মুযদালিফাহ",
    "জামারাতে রমি",
    "কুরবানি ও হালক/কসর",
    "তাওয়াফে বিদা",
)

@Composable
fun HajjScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        SectionHeader(
            title = "হজ শিক্ষা",
            modifier = Modifier.padding(16.dp),
        )
        Text(
            text = "হজের বিস্তারিত বিধান মাজহাব ও নির্ভরযোগ্য আলেমের নির্দেশনা অনুযায়ী যাচাই করুন।",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(steps) { index, step ->
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        text = "${index + 1}. $step",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}
