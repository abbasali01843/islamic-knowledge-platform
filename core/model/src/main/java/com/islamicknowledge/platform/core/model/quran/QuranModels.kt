package com.islamicknowledge.platform.core.model.quran

data class Surah(
    val id: Int,
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameBengali: String,
    val revelationType: RevelationType,
    val ayahCount: Int,
    val revelationOrder: Int? = null,
)

enum class RevelationType {
    MECCAN,
    MEDINAN,
    UNKNOWN,
}

data class Ayah(
    val id: Long,
    val surahId: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val juz: Int? = null,
    val hizb: Int? = null,
    val page: Int? = null,
    val hasSajdah: Boolean = false,
)

data class Translation(
    val id: Long,
    val ayahId: Long,
    val language: String,
    val translator: String,
    val text: String,
    val sourceId: String,
)

data class ContentSource(
    val sourceId: String,
    val publisher: String,
    val version: String,
    val license: String,
    val attribution: String,
    val sourceUrl: String,
)

data class QuranBookmark(
    val ayahId: Long,
    val note: String? = null,
    val createdAtEpochMs: Long,
)

data class LastReadPosition(
    val surahNumber: Int,
    val ayahNumber: Int,
    val updatedAtEpochMs: Long,
)
