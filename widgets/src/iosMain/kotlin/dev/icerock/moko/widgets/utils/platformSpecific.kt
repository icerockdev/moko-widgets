/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

@Suppress("NOTHING_TO_INLINE")
actual inline fun <T> platformSpecific(android: T, ios: T): T {
    return ios
}
