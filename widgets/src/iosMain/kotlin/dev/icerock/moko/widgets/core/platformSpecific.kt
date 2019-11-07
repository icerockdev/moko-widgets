package dev.icerock.moko.widgets.core

@Suppress("NOTHING_TO_INLINE")
actual inline fun <T> platformSpecific(android: T, ios: T): T {
    return ios
}
