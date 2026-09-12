package com.islamicknowledge.platform.feature.quran

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import com.islamicknowledge.platform.core.model.quran.RevelationType
import com.islamicknowledge.platform.core.model.quran.Surah

private enum class QuranLibraryTab { SURAHS, JUZ, PAGES, BOOKMARKS, NOTES }

@Composable
fun QuranScreen(onSurahClick: (Surah, Int?) -> Unit = { _, _ -> }) {
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
        query.isBlank() || it.nameBengali.contains(query, true) ||
            it.nameEnglish.contains(query, true) || it.nameArabic.contains(query)
    }
    val bookmarks = remember(libraryVersion) {
        preferences.getBookmarks().mapNotNull { key ->
            val parts = key.split(":")
            if (parts.size != 2) return@mapNotNull null
            val s = parts[0].toIntOrNull() ?: return@mapNotNull null
            val a = parts[1].toIntOrNull() ?: return@mapNotNull null
            val catalog = quranSurahs.firstOrNull { it.number == s } ?: return@mapNotNull null
            BookmarkEntry(catalog, repository.ayahsForSurah(s).firstOrNull { it.number == a }, a)
        }.sortedWith(compareBy({ it.surah.number }, { it.ayahNumber }))
    }
    val notes = remember(libraryVersion) {
        preferences.getAllNotes().mapNotNull { entry ->
            val catalog = quranSurahs.firstOrNull { it.number == entry.surahNumber } ?: return@mapNotNull null
            NoteListItem(catalog, entry)
        }
    }
    fun openJuz(juz: Int) {
        val hit = repository.firstAyahForJuz(juz) ?: return
        val surah = quranSurahs.firstOrNull { it.number == hit.first } ?: return
        onSurahClick(surah, hit.second.number)
    }
    fun openPage(page: Int) {
        val hit = repository.firstAyahForPage(page) ?: return
        val surah = quranSurahs.firstOrNull { it.number == hit.first } ?: return
        onSurahClick(surah, hit.second.number)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        SectionHeader(title = "কুরআন", modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                QuranLibraryTab.SURAHS to "সূরা",
                QuranLibraryTab.JUZ to "পারা",
                QuranLibraryTab.PAGES to "পৃষ্ঠা",
                QuranLibraryTab.BOOKMARKS to "বুকমার্ক",
                QuranLibraryTab.NOTES to "নোট",
            ).forEach { (tab, label) ->
                FilterChip(
                    selected = selectedTab == tab,
                    onClick = {
                        selectedTab = tab
                        if (tab == QuranLibraryTab.BOOKMARKS || tab == QuranLibraryTab.NOTES) libraryVersion++
                    },
                    label = { Text(label) },
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (selectedTab) {
            QuranLibraryTab.SURAHS -> {
                lastRead?.let { resume ->
                    val resumeSurah = quranSurahs.firstOrNull { it.number == resume.surahNumber }
                    if (resumeSurah != null) {
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("যেখান থেকে পড়া বন্ধ করেছিলেন", style = MaterialTheme.typography.labelMedium)
                                    Text("${resumeSurah.nameBengali} • আয়াত ${resume.ayahNumber}", style = MaterialTheme.typography.titleMedium)
                                }
                                Button(onClick = { onSurahClick(resumeSurah, resume.ayahNumber) }) { Text("Resume") }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                OutlinedTextField(
                    value = query, onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    singleLine = true, label = { Text("সূরা খুঁজুন") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                )
                Text("১১৪টি সূরা • ${filtered.size}টি ফলাফল", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered, key = { it.id }) { surah ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { onSurahClick(surah, null) }) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${surah.number}. ${surah.nameBengali}", style = MaterialTheme.typography.titleMedium)
                                    Text("${surah.nameEnglish} • ${surah.ayahCount} আয়াত", style = MaterialTheme.typography.bodySmall)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(surah.nameArabic, style = MaterialTheme.typography.titleLarge)
                                    Text(if (surah.revelationType == RevelationType.MECCAN) "মাক্কী" else "মাদানী", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
            QuranLibraryTab.JUZ -> {
                if (!hasIndex) EmptyMsg("পারা সূচি পুরো কনটেন্ট প্যাকেজে উপলব্ধ", "CI বিল্ডের পর ৩০ পারার নেভিগেশন চালু হবে।")
                else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(juzNumbers, key = { it }) { juz ->
                        val hit = repository.firstAyahForJuz(juz)
                        val sub = hit?.let { (sn, ay) ->
                            val name = quranSurahs.firstOrNull { it.number == sn }?.nameBengali ?: "সূরা $sn"
                            "$name • আয়াত ${ay.number}"
                        } ?: "শুরুর আয়াত"
                        Card(modifier = Modifier.fillMaxWidth().clickable { openJuz(juz) }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("পারা $juz", style = MaterialTheme.typography.titleMedium)
                                Text(sub, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            QuranLibraryTab.PAGES -> {
                if (!hasIndex) EmptyMsg("পৃষ্ঠা সূচি পুরো কনটেন্ট প্যাকেজে উপলব্ধ", "পূর্ণ ৬০৪ পৃষ্ঠার সূচি CI বিল্ডে জেনারেট হয়।")
                else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(pageNumbers, key = { it }) { page ->
                        val hit = repository.firstAyahForPage(page)
                        val sub = hit?.let { (sn, ay) ->
                            val name = quranSurahs.firstOrNull { it.number == sn }?.nameBengali ?: "সূরা $sn"
                            "$name • আয়াত ${ay.number}"
                        } ?: "শুরুর আয়াত"
                        Card(modifier = Modifier.fillMaxWidth().clickable { openPage(page) }) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("পৃষ্ঠা $page", style = MaterialTheme.typography.titleMedium)
                                Text(sub, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            QuranLibraryTab.BOOKMARKS -> {
                if (bookmarks.isEmpty()) EmptyMsg("এখনও কোনো আয়াত বুকমার্ক করা হয়নি।", null)
                else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(bookmarks, key = { "${it.surah.number}:${it.ayahNumber}" }) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f).clickable { onSurahClick(item.surah, item.ayahNumber) }) {
                                    Text("${item.surah.number}. ${item.surah.nameBengali} • আয়াত ${item.ayahNumber}", style = MaterialTheme.typography.titleMedium)
                                    Text(item.ayah?.bengali ?: "আয়াত খুলতে ট্যাপ করুন", style = MaterialTheme.typography.bodyLarge)
                                }
                                IconButton(onClick = { preferences.removeBookmark(item.surah.number, item.ayahNumber); libraryVersion++ }) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "বুকমার্ক মুছুন")
                                }
                            }
                        }
                    }
                }
            }
            QuranLibraryTab.NOTES -> {
                if (notes.isEmpty()) EmptyMsg("এখনও কোনো নোট সংরক্ষণ করা হয়নি।", null)
                else LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(notes, key = { "${it.entry.surahNumber}:${it.entry.ayahNumber}" }) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f).clickable { onSurahClick(item.surah, item.entry.ayahNumber) }) {
                                    Text("${item.surah.number}. ${item.surah.nameBengali} • আয়াত ${item.entry.ayahNumber}", style = MaterialTheme.typography.titleMedium)
                                    Text(item.entry.text, style = MaterialTheme.typography.bodyLarge)
                                }
                                IconButton(onClick = { preferences.deleteNote(item.entry.surahNumber, item.entry.ayahNumber); libraryVersion++ }) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "নোট মুছুন")
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
private fun EmptyMsg(title: String, body: String?) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        if (body != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(body, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuranReaderScreen(
    surah: Surah,
    initialAyah: Int? = null,
    onBack: () -> Unit,
    onNavigateToSurah: (Surah) -> Unit = {},
) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? ComponentActivity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }
    val clipboard = LocalClipboardManager.current
    val repository = remember(context) { QuranReaderRepository(context) }
    val preferences = remember(context) { QuranReaderPreferences(context) }
    val allAyahs = remember(repository, surah.number) { repository.ayahsForSurah(surah.number) }
    val previousSurah = quranSurahs.firstOrNull { it.number == surah.number - 1 }
    val nextSurah = quranSurahs.firstOrNull { it.number == surah.number + 1 }
    val listState = rememberLazyListState()
    var query by remember { mutableStateOf("") }
    var showArabic by remember { mutableStateOf(preferences.getShowArabic()) }
    var showBengali by remember { mutableStateOf(preferences.getShowBengali()) }
    var fontScale by remember { mutableStateOf(preferences.getFontScale()) }
    var bookmarkedKeys by remember {
        mutableStateOf(allAyahs.filter { preferences.isBookmarked(surah.number, it.number) }.map { it.number }.toSet())
    }
    var noteAyah by remember { mutableStateOf<ReaderAyah?>(null) }
    var noteVersion by remember { mutableStateOf(0) }
    val ayahs = allAyahs.filter {
        query.isBlank() || it.arabic.contains(query, true) || it.bengali.contains(query, true)
    }
    val progress by remember {
        derivedStateOf {
            if (ayahs.isEmpty()) 0f
            else {
                val first = listState.firstVisibleItemIndex.coerceIn(0, (ayahs.size - 1).coerceAtLeast(0))
                (first + 1).toFloat() / ayahs.size.toFloat()
            }
        }
    }
    LaunchedEffect(listState.firstVisibleItemIndex, ayahs) {
        val visible = ayahs.getOrNull(listState.firstVisibleItemIndex) ?: return@LaunchedEffect
        preferences.saveLastRead(surah.number, visible.number)
    }
    LaunchedEffect(initialAyah, ayahs.size) {
        val target = initialAyah ?: return@LaunchedEffect
        val index = ayahs.indexOfFirst { it.number == target }
        if (index >= 0) listState.scrollToItem(index)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "ফিরে যান") }
            Column(modifier = Modifier.weight(1f)) {
                Text(surah.nameBengali, style = MaterialTheme.typography.titleLarge)
                Text("${surah.nameArabic} • ${surah.ayahCount} আয়াত", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { previousSurah?.let(onNavigateToSurah) }, enabled = previousSurah != null) {
                Icon(Icons.AutoMirrored.Rounded.NavigateBefore, contentDescription = "পূর্ববর্তী সূরা")
            }
            IconButton(onClick = { nextSurah?.let(onNavigateToSurah) }, enabled = nextSurah != null) {
                Icon(Icons.AutoMirrored.Rounded.NavigateNext, contentDescription = "পরবর্তী সূরা")
            }
        }
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(3.dp))
        Text(repository.sourceAttribution(), style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterChip(selected = showArabic, onClick = { showArabic = !showArabic; preferences.setShowArabic(showArabic) }, label = { Text("عربي") })
            FilterChip(selected = showBengali, onClick = { showBengali = !showBengali; preferences.setShowBengali(showBengali) }, label = { Text("বাংলা") })
            IconButton(onClick = { fontScale = (fontScale - 0.1f).coerceAtLeast(0.8f); preferences.setFontScale(fontScale) }) {
                Icon(Icons.Rounded.Remove, contentDescription = "ফন্ট ছোট")
            }
            Text("${(fontScale * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
            IconButton(onClick = { fontScale = (fontScale + 0.1f).coerceAtMost(1.5f); preferences.setFontScale(fontScale) }) {
                Icon(Icons.Rounded.Add, contentDescription = "ফন্ট বড়")
            }
        }
        OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), singleLine = true, label = { Text("এই সূরার আয়াত খুঁজুন") })
        if (ayahs.isEmpty()) {
            Text(
                if (allAyahs.isEmpty()) "এই সূরার আয়াত ডেটা পাওয়া যায়নি।" else "কোনো আয়াত মেলেনি।",
                modifier = Modifier.fillMaxSize().padding(24.dp),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(state = listState, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(ayahs, key = { it.number }) { ayah ->
                    val bookmarked = ayah.number in bookmarkedKeys
                    val note = remember(noteVersion, surah.number, ayah.number) { preferences.getNote(surah.number, ayah.number) }
                    val text = buildString {
                        append("${surah.nameBengali} ${surah.number}:${ayah.number}\n")
                        if (showArabic) append("${ayah.arabic}\n")
                        if (showBengali) append(ayah.bengali)
                    }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("আয়াত ${ayah.number}", style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    val v = preferences.toggleBookmark(surah.number, ayah.number)
                                    bookmarkedKeys = if (v) bookmarkedKeys + ayah.number else bookmarkedKeys - ayah.number
                                }) {
                                    Icon(if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder, contentDescription = "বুকমার্ক")
                                }
                                IconButton(onClick = { noteAyah = ayah }) { Icon(Icons.Rounded.NoteAdd, contentDescription = "নোট") }
                                IconButton(onClick = { clipboard.setText(AnnotatedString(text)) }) { Icon(Icons.Rounded.ContentCopy, contentDescription = "কপি") }
                                IconButton(onClick = { shareText(context, text) }) { Icon(Icons.Rounded.Share, contentDescription = "শেয়ার") }
                            }
                            if (ayah.hasSajdah) Text("সিজদাহর আয়াত", style = MaterialTheme.typography.labelMedium)
                            val meta = listOfNotNull(ayah.page?.let { "পৃষ্ঠা $it" }, ayah.juz?.let { "পারা $it" }, ayah.hizb?.let { "হিজব $it" }).joinToString(" • ")
                            if (meta.isNotBlank()) Text(meta, style = MaterialTheme.typography.labelSmall)
                            if (showArabic) {
                                Text(
                                    ayah.arabic,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontSize = (MaterialTheme.typography.headlineSmall.fontSize.value * fontScale).sp,
                                        lineHeight = (MaterialTheme.typography.headlineSmall.fontSize.value * fontScale * 1.85f).sp,
                                        textDirection = TextDirection.Rtl,
                                    ),
                                    textAlign = TextAlign.End,
                                )
                            }
                            if (showBengali) {
                                Text(ayah.bengali, style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * fontScale))
                            }
                            if (note.isNotBlank()) Text("নোট: $note", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 10.dp))
                        }
                    }
                }
            }
        }
    }
    noteAyah?.let { ayah ->
        var noteText by remember(ayah.number) { mutableStateOf(preferences.getNote(surah.number, ayah.number)) }
        AlertDialog(
            onDismissRequest = { noteAyah = null },
            title = { Text("আয়াত ${ayah.number}-এর নোট") },
            text = { OutlinedTextField(value = noteText, onValueChange = { noteText = it }, modifier = Modifier.fillMaxWidth(), minLines = 3, label = { Text("আপনার নোট") }) },
            confirmButton = {
                TextButton(onClick = {
                    preferences.saveNote(surah.number, ayah.number, noteText)
                    noteVersion++
                    noteAyah = null
                }) { Text("সংরক্ষণ") }
            },
            dismissButton = { TextButton(onClick = { noteAyah = null }) { Text("বাতিল") } },
        )
    }
}

private fun shareText(context: Context, text: String) {
    context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }, "আয়াত শেয়ার করুন"))
}

private data class BookmarkEntry(val surah: Surah, val ayah: ReaderAyah?, val ayahNumber: Int)
private data class NoteListItem(val surah: Surah, val entry: NoteEntry)
