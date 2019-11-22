/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
import platform.UIKit.clipsToBounds
import platform.UIKit.contentMode
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual var imageWidgetViewFactory: VFC<ImageWidget> = { _, widget ->
    val style = widget.style

    UIImageView().apply {
        translatesAutoresizingMaskIntoConstraints = false

        widget.image.bind { it.apply(this) }

        when (style.scaleType) {
            ImageWidget.ScaleType.FILL -> this.contentMode =
                UIViewContentMode.UIViewContentModeScaleAspectFill
            ImageWidget.ScaleType.FIT -> this.contentMode =
                UIViewContentMode.UIViewContentModeScaleAspectFit
        }

        layer.masksToBounds = true
        clipsToBounds = true
    }
}