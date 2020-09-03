/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.darwin.NSObject

internal expect class DismissMailControllerDelegate() : NSObject,
    MFMailComposeViewControllerDelegateProtocol
