package com.luczka.mycoffee.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luczka.mycoffee.R

val switzerFontFamily = FontFamily(
    Font(R.font.switzer_light, FontWeight.Light),
    Font(R.font.switzer_regular, FontWeight.Normal),
    Font(R.font.switzer_medium, FontWeight.Medium),
    Font(R.font.switzer_semibold, FontWeight.SemiBold),
    Font(R.font.switzer_bold, FontWeight.Bold)
)

//val nunitoFontFamily = FontFamily(
//    Font(R.font.nunito_light, FontWeight.Light),
//    Font(R.font.nunito_regular, FontWeight.Normal),
//    Font(R.font.nunito_medium, FontWeight.Medium),
//    Font(R.font.nunito_semibold, FontWeight.SemiBold),
//    Font(R.font.nunito_bold, FontWeight.Bold)
//)

//val switzerFontFamily = FontFamily(
//    Font(R.font.satoshi_light, FontWeight.Light),
//    Font(R.font.satoshi_regular, FontWeight.Normal),
//    Font(R.font.satoshi_medium, FontWeight.Medium),
//    Font(R.font.satoshi_bold, FontWeight.SemiBold),
//    Font(R.font.satoshi_black, FontWeight.Bold)
//)

//val switzerFontFamily = FontFamily(
//    Font(R.font.supreme_light, FontWeight.Light),
//    Font(R.font.supreme_regular, FontWeight.Normal),
//    Font(R.font.supreme_medium, FontWeight.Medium),
//    Font(R.font.supreme_bold, FontWeight.SemiBold),
//    Font(R.font.supreme_extrabold, FontWeight.Bold)
//)

val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = switzerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
)