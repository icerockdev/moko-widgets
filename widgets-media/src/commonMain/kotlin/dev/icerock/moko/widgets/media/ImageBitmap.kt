/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.media

import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.widgets.core.Image

expect fun Image.Companion.bitmap(bitmap: Bitmap): Image
