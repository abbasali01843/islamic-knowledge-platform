package com.islamicknowledge.platform.feature.dua

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

data class DuaItem(val id: String, val category: String, val title: String, val arabic: String, val meaning: String, val reference: String, val repeat: Int)

@Composable
fun DuaScreen(modifier: Modifier = Modifier) {
    var category by remember { mutableStateOf("ALL") }
    val categories = listOf("ALL" to "সকল", "MORNING_EVENING" to "সকাল-সন্ধ্যা", "SLEEP" to "ঘুম ও জাগরণ", "PRAYER_SALAH" to "সালাত পরবর্তী", "FOOD_DRINK" to "খাদ্য-পানীয়", "HOME_ENTER_EXIT" to "ঘর", "TRAVEL_MOSQUE" to "সফর ও মসজিদ", "DISTRESS_FORGIVENESS" to "বিপদ ও ক্ষমা", "SICKNESS_RUQYAH" to "রোগ ও রুকইয়াহ")
    val list = if (category == "ALL") DUAS else DUAS.filter { it.category == category }
    Column(modifier) {
        SectionHeader(title = "দোয়া ও জিকির", modifier = Modifier.padding(16.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(categories) { item -> FilterChip(selected = category == item.first, onClick = { category = item.first }, label = { Text(item.second) }) } }
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(list, key = { it.id }) { item -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(item.title, style = MaterialTheme.typography.titleMedium); Text(item.arabic, style = MaterialTheme.typography.titleLarge); Text(item.meaning, style = MaterialTheme.typography.bodyLarge); Text(item.reference + " • " + item.repeat + " বার", style = MaterialTheme.typography.labelMedium) } } } }
    }
}
