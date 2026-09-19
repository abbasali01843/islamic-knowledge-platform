package com.islamicknowledge.platform.feature.hadith

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

data class HadithItem(val id: String, val book: String, val number: Int, val chapter: String?, val narrator: String?, val arabic: String, val bengali: String, val grade: String?, val explanation: String?, val topic: String, val isNawawi40: Boolean, val nawawiNumber: Int?, val reference: String)

@Composable
fun HadithScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val list = if (query.isBlank()) HADITHS else HADITHS.filter { it.bengali.contains(query, ignoreCase = true) || it.book.contains(query, ignoreCase = true) || it.reference.contains(query, ignoreCase = true) || (it.chapter?.contains(query, ignoreCase = true) == true) || (it.narrator?.contains(query, ignoreCase = true) == true) }
    Column(modifier) {
        SectionHeader(title = "হাদিস", modifier = Modifier.padding(16.dp))
        OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("হাদিস খুঁজুন") }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(item.book + " • " + item.number, style = MaterialTheme.typography.titleMedium)
                    Text(item.chapter ?: "", style = MaterialTheme.typography.labelMedium)\n                    Text(item.narrator ?: "", style = MaterialTheme.typography.bodySmall)\n                    Text(item.arabic, style = MaterialTheme.typography.titleLarge)
                    Text(item.bengali, style = MaterialTheme.typography.bodyLarge)\n                    item.grade?.let { Text(it, style = MaterialTheme.typography.labelMedium) }\n                    item.explanation?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                    Text(item.reference, style = MaterialTheme.typography.labelMedium)
                }}
            }
        }
    }
}
