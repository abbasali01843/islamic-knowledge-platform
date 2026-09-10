package com.islamicknowledge.platform.feature.quran

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranReaderDataTest {
    @Test
    fun bundledReaderContainsInitialSurahs() {
        assertEquals(setOf(1, 2), quranReaderContentSurahNumbers)
        assertEquals(7, quranReaderAyahs(1).size)
        assertEquals(5, quranReaderAyahs(2).size)
    }

    @Test
    fun readerSearchMatchesArabicAndBengali() {
        assertTrue(quranReaderSearch("الٓمٓ").any { it.first == 2 && it.second.number == 1 })
        assertTrue(quranReaderSearch("মুত্তাকীদের").any { it.first == 2 && it.second.number == 2 })
    }

    @Test
    fun unknownSurahHasNoBundledAyahs() {
        assertTrue(quranReaderAyahs(114).isEmpty())
    }
}
