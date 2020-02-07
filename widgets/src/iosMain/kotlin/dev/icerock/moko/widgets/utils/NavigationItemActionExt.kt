/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.utils

import dev.icerock.moko.widgets.objc.setAssociatedObject
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle

fun NavigationBar.Normal.BarButton.toUIBarButtonItem(): UIBarButtonItem {
    val actionHandler = LambdaTarget(action)
    val button = UIBarButtonItem(
        image = icon.toUIImage(),
        style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
        target = actionHandler,
        action = NSSelectorFromString("action")
    )
    setAssociatedObject(button, actionHandler)
    return button
}