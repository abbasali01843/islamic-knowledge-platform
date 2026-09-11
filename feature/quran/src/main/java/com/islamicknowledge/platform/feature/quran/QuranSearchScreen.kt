package com.islamicknowledge.platform.feature.quran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.model.quran.Surah

@Composable
fun QuranSearchScreen(
    onResultClick: (Surah, Int) -> Unit,
) {
    val context = LocalContext.current
    val repository = remember(context) { QuranReaderRepository(context) }
    var query by remember { mutableStateOf("") }
    val normalizedQuery = query.trim()
    val results = remember(normalizedQuery, repository) {
        repository.search(normalizedQuery).take(100)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "কুরআন অনুসন্ধান",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Rounded.Clear, contentDescription = "অনুসন্ধান মুছুন")
                    }
                }
            },
            label = { Text("আরবি বা বাংলা লিখুন") },
        )
        Text(
            text = if (normalizedQuery.isBlank()) {
                "৬২৩৬ আয়াতের অফলাইন কনটেন্টে খুঁজুন"
            } else {
                "${results.size}টি ফলাফল দেখানো হচ্ছে (সর্বোচ্চ ১০০টি)"
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        )
        if (normalizedQuery.isNotBlank() && results.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null)
                Text(
                    "কোনো আয়াত পাওয়া যায়নি।",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    "বাংলা বা আরবি শব্দের বানান পরিবর্তন করে আবার চেষ্টা করুন।",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        } else if (normalizedQuery.isBlank()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null)
                Text(
                    "কুরআনের আয়াত খুঁজুন",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp),
                )
                Text(
                    "আরবি বা বাংলা শব্দ লিখে ৬২৩৬টি অফলাইন আয়াতের মধ্যে অনুসন্ধান করুন।",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(results, key = { "${it.first}:${it.second.number}" }) { (surahNumber, ayah) ->
                    val surah = quranSurahs.firstOrNull { it.number == surahNumber } ?: return@items
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onResultClick(surah, ayah.number) },
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "${surah.number}. ${surah.nameBengali} • আয়াত ${ayah.number}",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                            Text(
                                ayah.arabic,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 8.dp),
                            )
                            Text(
                                ayah.bengali,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
