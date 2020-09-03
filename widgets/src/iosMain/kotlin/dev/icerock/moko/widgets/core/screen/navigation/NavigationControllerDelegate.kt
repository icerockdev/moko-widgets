/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen.navigation

import kotlinx.coroutines.Runnable
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

internal expect class NavigationControllerDelegate actual constructor(
    navigationScreen: NavigationScreen<*>
) : NSObject, UINavigationControllerDelegateProtocol {
    val resultCallbacks: MutableMap<UIViewController, Runnable>
}
