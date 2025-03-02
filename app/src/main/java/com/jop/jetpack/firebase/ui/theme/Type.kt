package com.jop.jetpack.firebase.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.jop.jetpack.firebase.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily), // 57
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily), // 45
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily), // 36

    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily), // 32
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily), // 28
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily), // 24

    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily), // 22
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily), // 16
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily), // 14

    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily), // 16
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily), // 14
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily), // 12

    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily), // 14
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily), // 12
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily), // 11
)

