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
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import com.islamicknowledge.platform.core.model.quran.RevelationType
import com.islamicknowledge.platform.core.model.quran.Surah

private val foundationSurahs = listOf(
    Surah(1, 1, "الفاتحة", "Al-Fatihah", "আল-ফাতিহা", RevelationType.MECCAN, 7, 5),
    Surah(2, 2, "البقرة", "Al-Baqarah", "আল-বাকারা", RevelationType.MEDINAN, 286, 87),
    Surah(3, 3, "آل عمران", "Ali 'Imran", "আলে ইমরান", RevelationType.MEDINAN, 200, 89),
    Surah(4, 4, "النساء", "An-Nisa", "আন-নিসা", RevelationType.MEDINAN, 176, 92),
    Surah(5, 5, "المائدة", "Al-Ma'idah", "আল-মায়িদাহ", RevelationType.MEDINAN, 120, 112),
)

@Composable
fun QuranScreen(
    onSurahClick: (Surah) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    val filtered = foundationSurahs.filter {
        query.isBlank() || it.nameBengali.contains(query, ignoreCase = true) ||
            it.nameEnglish.contains(query, ignoreCase = true) || it.nameArabic.contains(query)
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
            IconButton(onClick = { /* Search field is always available in this foundation. */ }) {
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
            text = "ফাউন্ডেশন ডেটা — পূর্ণ ১১৪ সূরার লাইসেন্স-ভেরিফায়েড ডেটাসেট পরবর্তী ধাপে যুক্ত হবে",
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
                Text(
                    text = surah.nameArabic,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = if (surah.revelationType == RevelationType.MECCAN) "মাক্কী" else "মাদানী",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
