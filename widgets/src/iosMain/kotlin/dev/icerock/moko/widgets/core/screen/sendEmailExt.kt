/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.screen

import dev.icerock.moko.widgets.core.associatedObject
import dev.icerock.moko.widgets.core.utils.DismissMailControllerDelegate
import platform.Foundation.NSError
import platform.MessageUI.MFMailComposeResult
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.darwin.NSObject

actual fun Screen<*>.sendEmail(
    email: String,
    subject: String,
    body: String
) {
    if (!MFMailComposeViewController.canSendMail()) {
        println("MailCompose can't send mail")
        return
    }

    val mail = MFMailComposeViewController()
    val manager = DismissMailControllerDelegate()
    mail.mailComposeDelegate = manager
    mail.associatedObject = manager
    mail.setToRecipients(listOf(email))
    mail.setSubject(subject)
    mail.setMessageBody(body, false)

    viewController.presentViewController(mail, true, null)
}
