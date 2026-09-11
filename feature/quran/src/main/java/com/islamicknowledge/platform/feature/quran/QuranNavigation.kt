package com.islamicknowledge.platform.feature.quran

import com.islamicknowledge.platform.core.model.quran.Surah

/** Public navigation API for consumers of the Quran feature module. */
fun findQuranSurah(number: Int): Surah? = quranSurahs.firstOrNull { it.number == number }
