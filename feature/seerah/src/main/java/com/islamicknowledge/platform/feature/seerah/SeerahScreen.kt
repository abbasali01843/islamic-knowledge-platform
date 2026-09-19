package com.islamicknowledge.platform.feature.seerah

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

private val events = listOf(
    "জন্ম ও শৈশব",
    "ওহি ও নবুয়ত",
    "মক্কী জীবন",
    "হিজরত",
    "মদীনার জীবন",
    "বদর, উহুদ ও খন্দক",
    "হুদাইবিয়া ও মক্কা বিজয়",
    "বিদায় হজ",
    "ইন্তেকাল"
)

@Composable
fun SeerahScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        SectionHeader(
            title = "সীরাতুন নবী ﷺ",
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(events) { event ->
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        event,
                        Modifier.padding(18.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
