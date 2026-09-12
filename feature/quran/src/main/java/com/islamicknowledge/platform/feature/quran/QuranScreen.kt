package com.islamicknowledge.platform.feature.quran

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.NoteAdd
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import com.islamicknowledge.platform.core.model.quran.RevelationType
import com.islamicknowledge.platform.core.model.quran.Surah

private enum class QuranLibraryTab {
    SURAHS,
    JUZ,
    PAGES,
    BOOKMARKS,
    NOTES,
}

@Composable
fun QuranScreen(
    onSurahClick: (Surah, Int?) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val preferences = remember(context) { QuranReaderPreferences(context) }
    val repository = remember(context) { QuranReaderRepository(context) }
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(QuranLibraryTab.SURAHS) }
    var libraryVersion by remember { mutableStateOf(0) }
    val lastRead = preferences.getLastRead()
    val hasIndex = remember(repository) { repository.hasStructuralIndex() }
    val juzNumbers = remember(repository) { repository.availableJuzNumbers().ifEmpty { (1..30).toList() } }
    val pageNumbers = remember(repository) { repository.availablePageNumbers().ifEmpty { (1..604).toList() } }

    val filtered = quranSurahs.filter {
        query.isBlank() || it.nameBengali.contains(query, ignoreCase = true) ||
            it.nameEnglish.contains(query, ignoreCase = true) || it.nameArabic.contains(query)
    }

    val bookmarks = remember(libraryVersion) {
        preferences.getBookmarks().mapNotNull { key ->
            val parts = key.split(":")
            if (parts.size != 2) return@mapNotNull null
            val surahNumber = parts[0].toIntOrNull() ?: return@mapNotNull null
            val ayahNumber = parts[1].toIntOrNull() ?: return@mapNotNull null
            val catalog = quranSurahs.firstOrNull { it.number == surahNumber } ?: return@mapNotNull null
            val ayah = repository.ayahsForSurah(surahNumber).firstOrNull { it.number == ayahNumber }
            BookmarkEntry(catalog, ayah, ayahNumber)
        }.sortedWith(compareBy({ it.surah.number }, { it.ayahNumber }))
    }

    val notes = remember(libraryVersion) {
        preferences.getAllNotes().mapNotNull { entry ->
            val catalog = quranSurahs.firstOrNull { it.number == entry.surahNumber } ?: return@mapNotNull null
            NoteListItem(catalog, entry)
        }
    }

    fun openJuz(juz: Int) {
        val hit = repository.firstAyahForJuz(juz)
        if (hit != null) {
            val surah = quranSurahs.firstOrNull { it.number == hit.first } ?: return
            onSurahClick(surah, hit.second.number)
        }
    }

    fun openPage(page: Int) {
        val hit = repository.firstAyahForPage(page)
        if (hit != null) {
            val surah = quranSurahs.firstOrNull { it.number == hit.first } ?: return
            onSurahClick(surah, hit.second.number)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SectionHeader(
            title = "কুরআন",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LibraryTabChip("সূরা", selectedTab == QuranLibraryTab.SURAHS) { selectedTab = QuranLibraryTab.SURAHS }
            LibraryTabChip("পারা", selectedTab == QuranLibraryTab.JUZ) { selectedTab = QuranLibraryTab.JUZ }
            LibraryTabChip("পৃষ্ঠা", selectedTab == QuranLibraryTab.PAGES) { selectedTab = QuranLibraryTab.PAGES }
            LibraryTabChip("বুকমার্ক", selectedTab == QuranLibraryTab.BOOKMARKS) {
                selectedTab = QuranLibraryTab.BOOKMARKS
                libraryVersion++
            }
            LibraryTabChip("নোট", selectedTab == QuranLibraryTab.NOTES) {
                selectedTab = QuranLibraryTab.NOTES
                libraryVersion++
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (selectedTab) {
            QuranLibraryTab.SURAHS -> {
                lastRead?.let { resume ->
                    val resumeSurah = quranSurahs.firstOrNull { it.number == resume.surahNumber }
                    if (resumeSurah != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .semantics { contentDescription = "শেষ পঠিত আয়াতে ফিরে যান" },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("যেখান থেকে পড়া বন্ধ করেছিলেন", style = MaterialTheme.typography.labelMedium)
                                    Text(
                                        "${resumeSurah.nameBengali} • আয়াত ${resume.ayahNumber}",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                }
                                Button(onClick = { onSurahClick(resumeSurah, resume.ayahNumber) }) {
                                    Text("Resume")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    singleLine = true,
                    label = { Text("সূরা খুঁজুন") },
                    placeholder = { Text("বাংলা, English বা Arabic") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
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
                        SurahRow(surah = surah, onClick = { onSurahClick(surah, null) })
                    }
                }
            }

            QuranLibraryTab.JUZ -> {
                if (!hasIndex) {
                    IndexUnavailableMessage(
                        title = "পারা সূচি পুরো কনটেন্ট প্যাকেজে উপলব্ধ",
                        body = "CI বা লোকাল জেনারেশনের পরে ৩০ পারার নেভিগেশন চালু হবে। এখন সূরা ট্যাব ব্যবহার করুন।",
                    )
                } else {
                    Text(
                        "৩০ পারা • ট্যাপ করে প্রথম আয়াতে যান",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(juzNumbers, key = { it }) { juz ->
                            IndexRow(
                                title = "পারা $juz",
                                subtitle = repository.firstAyahForJuz(juz)?.let { (surahNo, ayah) ->
                                    val name = quranSurahs.firstOrNull { it.number == surahNo }?.nameBengali ?: "সূরা $surahNo"
                                    "$name • আয়াত ${ayah.number}"
                                } ?: "শুরুর আয়াত",
                                onClick = { openJuz(juz) },
                                contentDescription = "পারা $juz খুলুন",
                            )
                        }
                    }
                }
            }

            QuranLibraryTab.PAGES -> {
                if (!hasIndex) {
                    IndexUnavailableMessage(
                        title = "পৃষ্ঠা সূচি পুরো কনটেন্ট প্যাকেজে উপলব্ধ",
                        body = "পূর্ণ ৬০৪ পৃষ্ঠার সূচি CI বিল্ডে জেনারেট হয়। লোকাল স্ট্যাব অ্যাসেটে পৃষ্ঠা মেটাডেটা নেই।",
                    )
                } else {
                    Text(
                        "মushaf পৃষ্ঠা • ট্যাপ করে সেই পৃষ্ঠার প্রথম আয়াতে যান",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(pageNumbers, key = { it }) { page ->
                            IndexRow(
                                title = "পৃষ্ঠা $page",
                                subtitle = repository.firstAyahForPage(page)?.let { (surahNo, ayah) ->
                                    val name = quranSurahs.firstOrNull { it.number == surahNo }?.nameBengali ?: "সূরা $surahNo"
                                    "$name • আয়াত ${ayah.number}"
                                } ?: "শুরুর আয়াত",
                                onClick = { openPage(page) },
                                contentDescription = "পৃষ্ঠা $page খুলুন",
                            )
                        }
                    }
                }
            }

            QuranLibraryTab.BOOKMARKS -> {
                if (bookmarks.isEmpty()) {
                    EmptyLibraryMessage("এখনও কোনো আয়াত বুকমার্ক করা হয়নি।")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(bookmarks, key = { "${it.surah.number}:${it.ayahNumber}" }) { item ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { onSurahClick(item.surah, item.ayahNumber) },
                                        ) {
                                            Text(
                                                "${item.surah.number}. ${item.surah.nameBengali} • আয়াত ${item.ayahNumber}",
                                                style = MaterialTheme.typography.titleMedium,
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                item.ayah?.bengali ?: "আয়াত খুলতে ট্যাপ করুন",
                                                style = MaterialTheme.typography.bodyLarge,
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                preferences.removeBookmark(item.surah.number, item.ayahNumber)
                                                libraryVersion++
                                            },
                                            modifier = Modifier.semantics {
                                                contentDescription = "বুকমার্ক মুছুন"
                                            },
                                        ) {
                                            Icon(Icons.Rounded.Delete, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            QuranLibraryTab.NOTES -> {
                if (notes.isEmpty()) {
                    EmptyLibraryMessage("এখনও কোনো নোট সংরক্ষণ করা হয়নি।")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(notes, key = { "${it.entry.surahNumber}:${it.entry.ayahNumber}" }) { item ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    onSurahClick(item.surah, item.entry.ayahNumber)
                                                },
                                        ) {
                                            Text(
                                                "${item.surah.number}. ${item.surah.nameBengali} • আয়াত ${item.entry.ayahNumber}",
                                                style = MaterialTheme.typography.titleMedium,
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(item.entry.text, style = MaterialTheme.typography.bodyLarge)
                                        }
                                        IconButton(
                                            onClick = {
                                                preferences.deleteNote(item.entry.surahNumber, item.entry.ayahNumber)
                                                libraryVersion++
                                            },
                                            modifier = Modifier.semantics {
                                                contentDescription = "নোট মুছুন"
                                            },
                                        ) {
                                            Icon(Icons.Rounded.Delete, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryTabChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = Modifier.semantics { contentDescription = "$label ট্যাব" },
    )
}

@Composable
private fun IndexRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    contentDescription: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun IndexUnavailableMessage(title: String, body: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}

@Composable
private fun EmptyLibraryMessage(message: String) {
    Text(
        message,
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun SurahRow(surah: Surah, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics { contentDescription = "সূরা ${surah.nameBengali} খুলুন" },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${surah.number}. ${surah.nameBengali}", style = MaterialTheme.typography.titleMedium)
                Text("${surah.nameEnglish} • ${surah.ayahCount} আয়াত", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(surah.nameArabic, style = MaterialTheme.typography.titleLarge)
                Text(
                    if (surah.revelationType == RevelationType.MECCAN) "মাক্কী" else "মাদানী",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun QuranReaderScreen(
    surah: Surah,
    initialAyah: Int? = null,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val repository = remember(context) { QuranReaderRepository(context) }
    val preferences = remember(context) { QuranReaderPreferences(context) }
    val allAyahs = remember(repository, surah.number) { repository.ayahsForSurah(surah.number) }
    val listState = rememberLazyListState()
    var query by remember { mutableStateOf("") }
    var showArabic by remember(preferences) { mutableStateOf(preferences.getShowArabic()) }
    var showBengali by remember(preferences) { mutableStateOf(preferences.getShowBengali()) }
    var fontScale by remember(preferences) { mutableStateOf(preferences.getFontScale()) }
    var bookmarkedKeys by remember {
        mutableStateOf(allAyahs.filter { preferences.isBookmarked(surah.number, it.number) }.map { it.number }.toSet())
    }
    var noteAyah by remember { mutableStateOf<ReaderAyah?>(null) }
    var noteVersion by remember { mutableStateOf(0) }
    val ayahs = allAyahs.filter {
        query.isBlank() || it.arabic.contains(query, ignoreCase = true) || it.bengali.contains(query, ignoreCase = true)
    }

    LaunchedEffect(initialAyah, ayahs.size) {
        val target = initialAyah ?: return@LaunchedEffect
        val index = ayahs.indexOfFirst { it.number == target }
        if (index >= 0) listState.scrollToItem(index)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.semantics { contentDescription = "সূরা তালিকায় ফিরে যান" },
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(surah.nameBengali, style = MaterialTheme.typography.titleLarge)
                Text("${surah.nameArabic} • ${surah.ayahCount} আয়াত", style = MaterialTheme.typography.bodySmall)
            }
        }
        Text(
            repository.sourceAttribution(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            FilterChip(
                selected = showArabic,
                onClick = {
                    showArabic = !showArabic
                    preferences.setShowArabic(showArabic)
                },
                label = { Text("عربي") },
            )
            FilterChip(
                selected = showBengali,
                onClick = {
                    showBengali = !showBengali
                    preferences.setShowBengali(showBengali)
                },
                label = { Text("বাংলা") },
            )
            IconButton(
                onClick = {
                    fontScale = (fontScale - 0.1f).coerceAtLeast(0.8f)
                    preferences.setFontScale(fontScale)
                },
                modifier = Modifier.semantics { contentDescription = "ফন্ট ছোট করুন" },
            ) {
                Icon(Icons.Rounded.Remove, contentDescription = null)
            }
            Text("${(fontScale * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
            IconButton(
                onClick = {
                    fontScale = (fontScale + 0.1f).coerceAtMost(1.5f)
                    preferences.setFontScale(fontScale)
                },
                modifier = Modifier.semantics { contentDescription = "ফন্ট বড় করুন" },
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            singleLine = true,
            label = { Text("এই সূরার আয়াত খুঁজুন") },
        )

        if (ayahs.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    if (allAyahs.isEmpty()) {
                        "এই সূরার আয়াত ডেটা কনটেন্ট প্যাকেজে পাওয়া যায়নি।"
                    } else {
                        "এই খোঁজার সাথে কোনো আয়াত মেলেনি।"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(ayahs, key = { it.number }) { ayah ->
                    val bookmarked = ayah.number in bookmarkedKeys
                    val note = remember(noteVersion, surah.number, ayah.number) {
                        preferences.getNote(surah.number, ayah.number)
                    }
                    val text = buildString {
                        append("${surah.nameBengali} ${surah.number}:${ayah.number}\n")
                        if (showArabic) append("${ayah.arabic}\n")
                        if (showBengali) append(ayah.bengali)
                    }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "আয়াত ${ayah.number}",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.weight(1f),
                                )
                                IconButton(
                                    onClick = {
                                        val newValue = preferences.toggleBookmark(surah.number, ayah.number)
                                        bookmarkedKeys =
                                            if (newValue) bookmarkedKeys + ayah.number else bookmarkedKeys - ayah.number
                                    },
                                    modifier = Modifier.semantics {
                                        contentDescription = if (bookmarked) "বুকমার্ক সরান" else "বুকমার্ক যোগ করুন"
                                    },
                                ) {
                                    Icon(
                                        if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                        contentDescription = null,
                                    )
                                }
                                IconButton(
                                    onClick = { noteAyah = ayah },
                                    modifier = Modifier.semantics { contentDescription = "নোট লিখুন" },
                                ) {
                                    Icon(Icons.Rounded.NoteAdd, contentDescription = null)
                                }
                                IconButton(
                                    onClick = { clipboard.setText(AnnotatedString(text)) },
                                    modifier = Modifier.semantics { contentDescription = "আয়াত কপি করুন" },
                                ) {
                                    Icon(Icons.Rounded.ContentCopy, contentDescription = null)
                                }
                                IconButton(
                                    onClick = { shareText(context, text) },
                                    modifier = Modifier.semantics { contentDescription = "আয়াত শেয়ার করুন" },
                                ) {
                                    Icon(Icons.Rounded.Share, contentDescription = null)
                                }
                            }
                            if (ayah.hasSajdah) {
                                Text("সিজদাহর আয়াত", style = MaterialTheme.typography.labelMedium)
                            }
                            val metadata = listOfNotNull(
                                ayah.page?.let { "পৃষ্ঠা $it" },
                                ayah.juz?.let { "পারা $it" },
                                ayah.hizb?.let { "হিজব $it" },
                            ).joinToString(" • ")
                            if (metadata.isNotBlank()) {
                                Text(metadata, style = MaterialTheme.typography.labelSmall)
                            }
                            if (showArabic) {
                                Text(
                                    ayah.arabic,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontSize = MaterialTheme.typography.headlineSmall.fontSize * fontScale,
                                    ),
                                    textAlign = TextAlign.End,
                                )
                            }
                            if (showBengali) {
                                Text(
                                    ayah.bengali,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize * fontScale,
                                    ),
                                )
                            }
                            if (note.isNotBlank()) {
                                Text(
                                    "নোট: $note",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 10.dp),
                                )
                            }
                            Text(
                                "পড়া হয়েছে হিসেবে সংরক্ষণ",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .clickable { preferences.saveLastRead(surah.number, ayah.number) }
                                    .semantics { contentDescription = "এই আয়াত শেষ পঠিত হিসেবে সংরক্ষণ করুন" },
                            )
                        }
                    }
                }
            }
        }
    }

    noteAyah?.let { ayah ->
        var noteText by remember(ayah.number) {
            mutableStateOf(preferences.getNote(surah.number, ayah.number))
        }
        AlertDialog(
            onDismissRequest = { noteAyah = null },
            title = { Text("আয়াত ${ayah.number}-এর নোট") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    label = { Text("আপনার নোট") },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        preferences.saveNote(surah.number, ayah.number, noteText)
                        noteVersion++
                        noteAyah = null
                    },
                ) { Text("সংরক্ষণ") }
            },
            dismissButton = {
                TextButton(onClick = { noteAyah = null }) { Text("বাতিল") }
            },
        )
    }
}

private fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "আয়াত শেয়ার করুন"))
}

private data class BookmarkEntry(
    val surah: Surah,
    val ayah: ReaderAyah?,
    val ayahNumber: Int,
)

private data class NoteListItem(
    val surah: Surah,
    val entry: NoteEntry,
)
