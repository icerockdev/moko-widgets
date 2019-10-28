package com.icerockdev.mpp.widgets.style.view

data class TextStyle(
    val size: Int = 15,
    val color: Int = 0xFF000000.toInt(),
    val fontStyle: FontStyle = FontStyle.MEDIUM
)

enum class FontStyle {
    BOLD, MEDIUM
}