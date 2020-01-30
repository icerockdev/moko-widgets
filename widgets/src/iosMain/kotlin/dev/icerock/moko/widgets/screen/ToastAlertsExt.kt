/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import kotlinx.cinterop.readValue
import dev.icerock.moko.resources.desc.StringDesc
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*


actual fun Screen<*>.showToast(message: StringDesc) {

    val containerView = UIView(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false

        layer.cornerRadius = 10.0
        clipsToBounds = true
        backgroundColor = UIColor(red = 0.0, green = 0.0, blue = 0.0, alpha = 0.4)
    }


    val toastLabel = UILabel(frame = CGRectZero.readValue()).apply {
        translatesAutoresizingMaskIntoConstraints = false

        numberOfLines = 0

        textAlignment = NSTextAlignmentCenter
        text = message.localized()
    }

    containerView.addSubview(toastLabel)
    viewController.view.addSubview(containerView)

    toastLabel.bottomAnchor.constraintEqualToAnchor(containerView.bottomAnchor, -8.0).active = true
    toastLabel.topAnchor.constraintEqualToAnchor(containerView.topAnchor, 8.0).active = true
    toastLabel.rightAnchor.constraintEqualToAnchor(containerView.rightAnchor, -16.0).active = true
    toastLabel.leftAnchor.constraintEqualToAnchor(containerView.leftAnchor, 16.0).active = true

    containerView.centerXAnchor.constraintEqualToAnchor(viewController.view.centerXAnchor).active =
        true
    containerView.bottomAnchor.constraintEqualToAnchor(viewController.view.safeAreaLayoutGuide.bottomAnchor, -60.0)
        .active = true

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