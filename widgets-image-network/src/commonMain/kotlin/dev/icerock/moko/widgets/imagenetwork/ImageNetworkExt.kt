/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.imagenetwork

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.widgets.core.Image

expect fun Image.Companion.network(
    url: String,
    placeholder: ImageResource? = null
): Image