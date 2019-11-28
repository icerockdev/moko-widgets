package dev.icerock.moko.widgets.utils

expect inline fun <T> platformSpecific(android: T, ios: T): T
