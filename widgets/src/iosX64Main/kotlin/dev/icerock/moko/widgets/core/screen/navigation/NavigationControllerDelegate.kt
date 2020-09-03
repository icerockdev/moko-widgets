/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen.navigation

import dev.icerock.moko.widgets.core.screen.getAssociatedScreen
import kotlinx.coroutines.Runnable
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.native.ref.WeakReference


internal actual class NavigationControllerDelegate actual constructor(
    navigationScreen: NavigationScreen<*>
) : NSObject(), UINavigationControllerDelegateProtocol {

    private val navigationScreen = WeakReference(navigationScreen)
    actual val resultCallbacks = mutableMapOf<UIViewController, Runnable>()

    override fun navigationController(
        navigationController: UINavigationController,
        willShowViewController: UIViewController,
        animated: Boolean
    ) {
        val stack = navigationController.viewControllers
        resultCallbacks.filterKeys { stack.contains(it).not() }
            .forEach { (vc, runnable) ->
                runnable.run()
                resultCallbacks.remove(vc)
            }
        val controller = willShowViewController
        val screen = controller.getAssociatedScreen() ?: return

        if (screen is NavigationItem) {
            navigationScreen.get()?.updateNavigation(screen, controller)
        }
    }
}
