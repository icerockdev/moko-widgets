/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.objc.setAssociatedObject
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
    val manager = EmailManager()
    mail.mailComposeDelegate = manager
    setAssociatedObject(mail, manager)
    mail.setToRecipients(listOf(email))
    mail.setSubject(subject)
    mail.setMessageBody(body, false)

    viewController.presentViewController(mail, true, null)
}

private class EmailManager : NSObject(), MFMailComposeViewControllerDelegateProtocol {
    override fun mailComposeController(
        controller: MFMailComposeViewController,
        didFinishWithResult: MFMailComposeResult,
        error: NSError?
    ) {
        controller.dismissViewControllerAnimated(true, null)
    }
}
