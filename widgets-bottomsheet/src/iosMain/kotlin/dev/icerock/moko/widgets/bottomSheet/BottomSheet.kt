/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.bottomsheet

import cocoapods.mokoWidgetsBottomSheet.BottomSheetController
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

actual fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>,
    onDismiss: () -> Unit
) {
    val view = content.buildView(viewController).view
    BottomSheetController().showOnViewController(
        vc = this.viewController,
        withContent = view,
        onDismiss = onDismiss
    )
}
