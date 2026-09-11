package com.islamicknowledge.platform.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val IslamicTypography = Typography(
    displaySmall = Typography().displaySmall.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineMedium = Typography().headlineMedium.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineSmall = Typography().headlineSmall.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleLarge = Typography().titleLarge.copy(
        fontSize = 21.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    titleMedium = Typography().titleMedium.copy(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontSize = 17.sp,
        lineHeight = 27.sp,
    ),
    bodyMedium = Typography().bodyMedium.copy(
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    bodySmall = Typography().bodySmall.copy(
        fontSize = 13.sp,
        lineHeight = 19.sp,
    ),
    labelLarge = Typography().labelLarge.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold,
    ),
)
