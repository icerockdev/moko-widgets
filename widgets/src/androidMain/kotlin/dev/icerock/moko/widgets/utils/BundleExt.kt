/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import android.os.Bundle

fun Bundle.getIntNullable(key: String): Int? {
    return if (containsKey(key)) getInt(key)
    else null
}
