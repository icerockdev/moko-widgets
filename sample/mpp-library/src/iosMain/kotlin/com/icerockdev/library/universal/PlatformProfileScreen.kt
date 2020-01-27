/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import cocoapods.mppLibraryIos.ProfileViewController
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.utils.bind
import platform.Foundation.NSBundle
import platform.UIKit.UIViewController

// here we can setup own create UIViewController logic
actual class PlatformProfileScreen actual constructor(
    override val backRoute: Route<Unit>
) : ProfileScreen() {

    override fun createViewController(): UIViewController {
        val viewController = ProfileViewController(
            bundle = NSBundle.bundleForClass(ProfileViewController.`class`()!!),
            nibName = null
        )
        viewController.loadViewIfNeeded()
        val textLabel = viewController.textLabel()!!
        profileViewModel.text.bind { textLabel.text = it.localized() }
        return viewController
    }
}
