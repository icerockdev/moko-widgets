package dev.icerock.moko.widgets.core

expect inline fun <T> platformSpecific(android: T, ios: T): T
