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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
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
import com.islamicknowledge.platform.core.design.components.SectionHeader
import com.islamicknowledge.platform.core.model.quran.RevelationType
import com.islamicknowledge.platform.core.model.quran.Surah

@Composable
fun QuranScreen(
    onSurahClick: (Surah) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    val filtered = quranSurahs.filter {
        query.isBlank() || it.nameBengali.contains(query, ignoreCase = true) ||
            it.nameEnglish.contains(query, ignoreCase = true) ||
            it.nameArabic.contains(query)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SectionHeader(title = "কুরআন")
            IconButton(onClick = { /* Search field is always available. */ }) {
                Icon(Icons.Rounded.Search, contentDescription = "কুরআন খুঁজুন")
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            label = { Text("সূরা খুঁজুন") },
            placeholder = { Text("বাংলা, English বা Arabic") },
        )

        Text(
            text = "১১৪টি সূরা • অফলাইন ক্যাটালগ • ${filtered.size}টি ফলাফল",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(filtered, key = { it.id }) { surah ->
                SurahRow(surah = surah, onClick = { onSurahClick(surah) })
            }
        }
    }
}

@Composable
private fun SurahRow(
    surah: Surah,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${surah.number}. ${surah.nameBengali}",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${surah.nameEnglish} • ${surah.ayahCount} আয়াত",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = surah.nameArabic, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = if (surah.revelationType == RevelationType.MECCAN) "মাক্কী" else "মাদানী",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun QuranReaderScreen(
    surah: Surah,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val preferences = remember(context) { QuranReaderPreferences(context) }
    val allAyahs = quranReaderAyahs(surah.number)
    var query by remember { mutableStateOf("") }
    var bookmarkedKeys by remember { mutableStateOf(allAyahs.filter { preferences.isBookmarked(surah.number, it.number) }.map { it.number }.toSet()) }
    val ayahs = allAyahs.filter {
        query.isBlank() || it.arabic.contains(query, ignoreCase = true) || it.bengali.contains(query, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "ফিরে যান")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(surah.nameBengali, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${surah.nameArabic} • ${surah.ayahCount} আয়াত",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        if (allAyahs.isNotEmpty()) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                label = { Text("এই সূরার আয়াত খুঁজুন") },
            )
        }

        if (ayahs.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (allAyahs.isEmpty()) "এই সূরার পূর্ণ আয়াত ডেটা পরবর্তী কনটেন্ট প্যাকেজে যুক্ত হবে।" else "এই খোঁজার সাথে কোনো আয়াত মেলেনি।",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(ayahs, key = { it.number }) { ayah ->
                    val bookmarked = ayah.number in bookmarkedKeys
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "আয়াত ${ayah.number}",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.weight(1f),
                                )
                                IconButton(onClick = {
                                    val newValue = preferences.toggleBookmark(surah.number, ayah.number)
                                    bookmarkedKeys = if (newValue) bookmarkedKeys + ayah.number else bookmarkedKeys - ayah.number
                                }) {
                                    Icon(
                                        imageVector = if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                        contentDescription = if (bookmarked) "বুকমার্ক সরান" else "বুকমার্ক করুন",
                                    )
                                }
                            }
                            Text(
                                text = ayah.arabic,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.End,
                            )
                            Text(text = ayah.bengali, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "পড়া হয়েছে হিসেবে সংরক্ষণ",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .clickable { preferences.saveLastRead(surah.number, ayah.number) },
                            )
                        }
                    }
                }
            }
        }
    }
}
