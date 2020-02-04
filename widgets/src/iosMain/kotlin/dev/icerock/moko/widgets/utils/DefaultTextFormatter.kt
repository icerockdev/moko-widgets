package dev.icerock.moko.widgets.utils

class DefaultTextFormatter (val textPattern: String, val patternSymbol: Char = '#') {

    fun format(unformattedText: String): String {

        var formatted = ""
        var unformattedIndex = 0
        var patternIndex = 0

        while (patternIndex < textPattern.length && unformattedIndex < unformattedText.length) {
            if (textPattern.length <= patternIndex) break
            val patternCharacter = textPattern[patternIndex]
            if (patternCharacter == patternSymbol) {
                if (unformattedIndex < unformattedText.length) {
                    formatted = formatted.plus(unformattedText[unformattedIndex])
                }
                unformattedIndex += 1
            } else {
                formatted = formatted.plus(patternCharacter)
            }
            patternIndex += 1
        }
        return formatted
    }

    fun unformat(formatted: String): String {
        var unformatted = ""
        var formattedIndex = 0

        while (formattedIndex < formatted.length) {
            if (formattedIndex < formatted.length) {
                val formattedCharacter = formatted[formattedIndex]
                if (formattedIndex >= textPattern.length) {
                    unformatted = unformatted.plus(formattedCharacter)
                } else if (formattedCharacter != textPattern[formattedIndex]) {
                    unformatted = unformatted.plus(formattedCharacter)
                }
                formattedIndex += 1
            }

        }
        return unformatted
    }
}