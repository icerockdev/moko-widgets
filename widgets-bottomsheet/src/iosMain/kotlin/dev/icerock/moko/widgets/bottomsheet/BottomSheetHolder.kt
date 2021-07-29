/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import dev.icerock.moko.widgets.core.View
import platform.UIKit.UIViewController

internal expect class BottomSheetHolder() : SelfDismisser {
    fun show(
        viewController: UIViewController,
        view: View,
        onDismiss: (isSelfDismissed: Boolean) -> Unit
    )
}
