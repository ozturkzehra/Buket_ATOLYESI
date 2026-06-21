package com.example.canimuygulamam

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val PlayFont = FontFamily(Font(R.font.play))
val InterFont = FontFamily(Font(R.font.inter))
val ItalianaFont = FontFamily(Font(R.font.italiana))

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = ItalianaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ItalianaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PlayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PlayFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
