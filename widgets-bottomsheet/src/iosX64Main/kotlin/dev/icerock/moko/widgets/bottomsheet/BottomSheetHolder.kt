/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import cocoapods.mokoWidgetsBottomSheet.BottomSheetController
import dev.icerock.moko.widgets.core.View
import platform.UIKit.UIViewController

internal actual class BottomSheetHolder : SelfDismisser {
    val bottomSheet = BottomSheetController()

    actual fun show(
        viewController: UIViewController,
        view: View,
        onDismiss: (isSelfDismissed: Boolean) -> Unit
    ) {
        bottomSheet.showOnViewController(
            vc = viewController,
            withContent = view,
            onDismiss = onDismiss
        )
    }

    override fun dismissSelf() {
        bottomSheet.dismiss()
    }
}
