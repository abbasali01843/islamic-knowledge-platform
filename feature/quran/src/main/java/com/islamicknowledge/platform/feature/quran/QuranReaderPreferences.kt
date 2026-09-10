package com.islamicknowledge.platform.feature.quran

import android.content.Context

internal class QuranReaderPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean =
        preferences.getStringSet(KEY_BOOKMARKS, emptySet()).orEmpty().contains(key(surahNumber, ayahNumber))

    fun toggleBookmark(surahNumber: Int, ayahNumber: Int): Boolean {
        val bookmarks = preferences.getStringSet(KEY_BOOKMARKS, emptySet()).orEmpty().toMutableSet()
        val bookmarkKey = key(surahNumber, ayahNumber)
        val bookmarked = if (bookmarks.add(bookmarkKey)) true else {
            bookmarks.remove(bookmarkKey)
            false
        }
        preferences.edit().putStringSet(KEY_BOOKMARKS, bookmarks).apply()
        return bookmarked
    }

    fun saveLastRead(surahNumber: Int, ayahNumber: Int) {
        preferences.edit()
            .putInt(KEY_LAST_SURAH, surahNumber)
            .putInt(KEY_LAST_AYAH, ayahNumber)
            .apply()
    }

    fun getLastRead(): LastRead? {
        val surah = preferences.getInt(KEY_LAST_SURAH, -1)
        val ayah = preferences.getInt(KEY_LAST_AYAH, -1)
        return if (surah > 0 && ayah > 0) LastRead(surah, ayah) else null
    }

    private fun key(surahNumber: Int, ayahNumber: Int): String = "$surahNumber:$ayahNumber"

    companion object {
        private const val FILE_NAME = "quran_reader_preferences"
        private const val KEY_BOOKMARKS = "bookmarks"
        private const val KEY_LAST_SURAH = "last_read_surah"
        private const val KEY_LAST_AYAH = "last_read_ayah"
    }
}

data class LastRead(
    val surahNumber: Int,
    val ayahNumber: Int,
)
