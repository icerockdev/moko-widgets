/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import platform.Foundation.NSError
import platform.MessageUI.MFMailComposeResult
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.darwin.NSObject


internal actual class DismissMailControllerDelegate : NSObject(),
    MFMailComposeViewControllerDelegateProtocol {

    override fun mailComposeController(
        controller: MFMailComposeViewController,
        didFinishWithResult: MFMailComposeResult,
        error: NSError?
    ) {
        controller.dismissViewControllerAnimated(true, null)
    }
}
