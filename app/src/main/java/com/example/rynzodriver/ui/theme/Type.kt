package com.example.rynzodriver.ui.theme

import androidx.compose.material3.Typography as MaterialTypography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rynzodriver.R

val InterFontFamily = FontFamily(
    Font(R.font.inter_variable, FontWeight.Normal),
    Font(R.font.inter_variable, FontWeight.Medium),
    Font(R.font.inter_variable, FontWeight.SemiBold),
    Font(R.font.inter_variable, FontWeight.Bold)
)

val InterTypography: MaterialTypography = MaterialTypography(
    displayLarge = TextStyle(fontFamily = InterFontFamily),
    displayMedium = TextStyle(fontFamily = InterFontFamily),
    displaySmall = TextStyle(fontFamily = InterFontFamily),
    headlineLarge = TextStyle(fontFamily = InterFontFamily),
    headlineMedium = TextStyle(fontFamily = InterFontFamily),
    headlineSmall = TextStyle(fontFamily = InterFontFamily),
    titleLarge = TextStyle(fontFamily = InterFontFamily),
    titleMedium = TextStyle(fontFamily = InterFontFamily),
    titleSmall = TextStyle(fontFamily = InterFontFamily),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(fontFamily = InterFontFamily),
    labelMedium = TextStyle(fontFamily = InterFontFamily),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp
    )
)

val Typography: MaterialTypography = InterTypography
