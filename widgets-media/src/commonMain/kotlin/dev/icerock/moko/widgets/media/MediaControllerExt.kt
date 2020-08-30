/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.media

import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.widgets.core.screen.Screen

expect fun Screen<*>.createMediaPickerController(permissionsController: PermissionsController): MediaPickerController

expect fun MediaPickerController.bind(screen: Screen<*>)
