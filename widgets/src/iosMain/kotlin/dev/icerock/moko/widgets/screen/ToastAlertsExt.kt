/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlinx.cinterop.readValue
import dev.icerock.moko.resources.desc.StringDesc
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.clipsToBounds
import platform.UIKit.backgroundColor
import platform.UIKit.UIColor
import platform.UIKit.UILabel
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIApplication
import platform.UIKit.addSubview
import platform.UIKit.UIWindow
import platform.UIKit.bottomAnchor
import platform.UIKit.topAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.leadingAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.animateWithDuration
import platform.UIKit.UIViewAnimationOptionTransitionNone
import platform.UIKit.alpha
import platform.UIKit.removeFromSuperview


actual fun Screen<*>.showToast(message: StringDesc) {

    val containerView = UIView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false

        layer.cornerRadius = 10.0
        clipsToBounds = true
        backgroundColor = UIColor(red = 0.8, green = 0.8, blue = 0.8, alpha = 0.8)
    }


    val toastLabel = UILabel(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false

        numberOfLines = 0

        textAlignment = NSTextAlignmentCenter
        text = message.localized()
    }

    containerView.addSubview(toastLabel)
    containerView.layer.zPosition = 10000.0
    val controller = (UIApplication.sharedApplication.windows.last() as? UIWindow)?.rootViewController
    (UIApplication.sharedApplication.windows.last() as? UIWindow)?.addSubview(containerView)

    toastLabel.bottomAnchor.constraintEqualToAnchor(containerView.bottomAnchor, -8.0).active = true
    toastLabel.topAnchor.constraintEqualToAnchor(containerView.topAnchor, 8.0).active = true
    toastLabel.rightAnchor.constraintEqualToAnchor(containerView.rightAnchor, -16.0).active = true
    toastLabel.leftAnchor.constraintEqualToAnchor(containerView.leftAnchor, 16.0).active = true

    if (controller != null) {
        containerView.centerXAnchor.constraintEqualToAnchor(controller.view.centerXAnchor).active =
            true
        containerView.bottomAnchor.constraintEqualToAnchor(
            controller.view.safeAreaLayoutGuide.bottomAnchor,
            -60.0
        )
            .active = true
        containerView.leadingAnchor.constraintGreaterThanOrEqualToAnchor(
            controller.view.leadingAnchor,
            24.0
        ).active = true
        controller.view.trailingAnchor.constraintGreaterThanOrEqualToAnchor(
            containerView.trailingAnchor,
            24.0
        ).active = true

    }
    UIView.animateWithDuration(
        duration = 1.0,
        delay = 3.0,
        animations = {
            containerView.alpha = 0.0
        },
        options = UIViewAnimationOptionTransitionNone,
        completion = {
            containerView.removeFromSuperview()
        }
    )
}