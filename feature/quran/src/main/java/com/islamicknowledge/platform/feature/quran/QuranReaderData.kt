package com.islamicknowledge.platform.feature.quran

data class ReaderAyah(
    val number: Int,
    val arabic: String,
    val bengali: String,
    val juz: Int? = null,
    val hizb: Int? = null,
    val page: Int? = null,
    val hasSajdah: Boolean = false,
)
