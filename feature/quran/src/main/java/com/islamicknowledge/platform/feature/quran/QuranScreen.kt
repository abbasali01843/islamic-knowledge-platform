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

@Composable
fun QuranReaderScreen(
    surah: Surah,
    onBack: () -> Unit,
) {
    val ayahs = if (surah.number == 1) alFatihahAyahs else emptyList()

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

        if (ayahs.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "এই সূরার আয়াত ডেটা পরবর্তী কনটেন্ট প্যাকেজে যুক্ত হবে।",
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
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "${ayah.number}",
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Text(
                                text = ayah.arabic,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.End,
                            )
                            Text(
                                text = ayah.bengali,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ReaderAyah(
    val number: Int,
    val arabic: String,
    val bengali: String,
)

private val alFatihahAyahs = listOf(
    ReaderAyah(1, "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ", "পরম করুণাময় অতি দয়ালু আল্লাহর নামে।"),
    ReaderAyah(2, "ٱلۡحَمۡدُ لِلَّهِ رَبِّ ٱلۡعَٰلَمِينَ", "সকল প্রশংসা কেবল আল্লাহর জন্য, যিনি সৃষ্টিকুলের রব।"),
    ReaderAyah(3, "ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ", "(যিনি) পরম করুণাময়, অতি দয়ালু।"),
    ReaderAyah(4, "مَٰلِكِ يَوۡمِ ٱلدِّينِ", "(যিনি) বিচার দিবসের মালিক।"),
    ReaderAyah(5, "إِيَّاكَ نَعۡبُدُ وَإِيَّاكَ نَسۡتَعِينُ", "আমরা শুধু আপনারই ‘ইবাদাত করি এবং শুধু আপনার নিকটই সাহায্য চাই।"),
    ReaderAyah(6, "ٱهۡدِنَا ٱلصِّرَٰطَ ٱلۡمُسۡتَقِيمَ", "আপনি আমাদেরকে সরল পথের হিদায়াত দিন।"),
    ReaderAyah(7, "صِرَٰطَ ٱلَّذِينَ أَنۡعَمۡتَ عَلَيۡهِمۡ غَيۡرِ ٱلۡمَغۡضُوبِ عَلَيۡهِمۡ وَلَا ٱلضَّآلِّينَ", "তাদের পথ, যাদেরকে আপনি নি‘আমত দিয়েছেন। তাদের পথ নয়, যাদের ওপর আপনার ক্রোধ নিপতিত হয়েছে এবং যারা পথভ্রষ্ট হয়ে গেছে।"),
)
