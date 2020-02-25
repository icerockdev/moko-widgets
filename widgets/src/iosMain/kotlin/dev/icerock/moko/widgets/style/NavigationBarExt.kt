/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package dev.icerock.moko.widgets.style

import dev.icerock.moko.graphics.toUIColor
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.utils.toUIFont
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationBar
import platform.UIKit.tintColor

fun UINavigationBar.applyNavigationBarStyle(style: NavigationBar.Styles?) {
    barTintColor = style?.backgroundColor?.toUIColor()

    if (style?.textStyle != null) {
        var textAttibutes = mapOf<Any?, Any?>()
        if (style.textStyle.color != null) {
            style.textStyle.color.toUIColor().CGColor
            textAttibutes = textAttibutes.plus(NSForegroundColorAttributeName to style.textStyle.color.toUIColor())
        }
        val font = style.textStyle.toUIFont()
        if (font != null) {
            textAttibutes = textAttibutes.plus(NSFontAttributeName to font)
        }

        titleTextAttributes = textAttibutes
    } else {
        titleTextAttributes = null
    }

    tintColor = style?.tintColor?.toUIColor()
        ?: UIApplication.sharedApplication.keyWindow?.rootViewController()?.view?.tintColor!!
}
