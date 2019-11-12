/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.widgets.core.VFC
import platform.UIKit.UIView

actual var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget> = { _, _ ->
    // TODO add factory implementation
    UIView()
}
