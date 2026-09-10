package com.islamicknowledge.platform.feature.quran

import android.content.Context
import org.json.JSONObject

internal class QuranReaderRepository(context: Context) {
    private val applicationContext = context.applicationContext
    private val document: QuranReaderDocument by lazy { loadDocument() }

    fun ayahsForSurah(surahNumber: Int): List<ReaderAyah> =
        document.surahs.firstOrNull { it.number == surahNumber }?.ayahs.orEmpty()

    fun search(query: String): List<Pair<Int, ReaderAyah>> {
        if (query.isBlank()) return emptyList()
        return document.surahs.flatMap { surah ->
            surah.ayahs.filter {
                it.arabic.contains(query, ignoreCase = true) ||
                    it.bengali.contains(query, ignoreCase = true)
            }.map { surah.number to it }
        }
    }

    fun sourceAttribution(): String =
        "বাংলা অনুবাদ: ${document.bengaliSource.name} • V${document.bengaliSource.version} • QuranEnc.com"

    private fun loadDocument(): QuranReaderDocument {
        val json = applicationContext.assets.open("quran_reader.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val bengaliSource = root.getJSONObject("bengaliSource")
        val surahsJson = root.getJSONArray("surahs")
        val surahs = buildList(surahsJson.length()) {
            for (index in 0 until surahsJson.length()) {
                val surahJson = surahsJson.getJSONObject(index)
                val ayahsJson = surahJson.getJSONArray("ayahs")
                val ayahs = buildList(ayahsJson.length()) {
                    for (ayahIndex in 0 until ayahsJson.length()) {
                        val ayah = ayahsJson.getJSONObject(ayahIndex)
                        add(
                            ReaderAyah(
                                number = ayah.getInt("number"),
                                arabic = ayah.getString("arabic"),
                                bengali = ayah.getString("bengali"),
                                juz = ayah.optionalInt("juz"),
                                hizb = ayah.optionalInt("hizb"),
                                page = ayah.optionalInt("page"),
                                hasSajdah = ayah.optBoolean("hasSajdah", false),
                            ),
                        )
                    }
                }
                add(QuranReaderSurah(surahJson.getInt("number"), ayahs))
            }
        }
        return QuranReaderDocument(
            surahs = surahs,
            bengaliSource = QuranSource(
                name = bengaliSource.getString("name"),
                version = bengaliSource.getString("version"),
            ),
        )
    }
}

private fun JSONObject.optionalInt(key: String): Int? =
    if (has(key) && !isNull(key)) optInt(key).takeIf { it > 0 } else null

private data class QuranReaderDocument(
    val surahs: List<QuranReaderSurah>,
    val bengaliSource: QuranSource,
)

private data class QuranReaderSurah(
    val number: Int,
    val ayahs: List<ReaderAyah>,
)

private data class QuranSource(
    val name: String,
    val version: String,
)
