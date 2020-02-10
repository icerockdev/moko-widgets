package dev.icerock.moko.widgets.bottomSheet

import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.core.View
import cocoapods.mokoWidgetsBottomSheet.BottomSheetController

actual fun Screen<*>.showBottomSheet(
    content: View
) {
    BottomSheetController().showOnViewController(this.viewController, content)
}