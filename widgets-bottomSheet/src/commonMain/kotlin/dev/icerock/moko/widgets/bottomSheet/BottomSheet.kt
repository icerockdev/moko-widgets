package dev.icerock.moko.widgets.bottomSheet

import dev.icerock.moko.widgets.core.View
import dev.icerock.moko.widgets.screen.Screen

expect fun Screen<*>.showBottomSheet(
    content: View
)