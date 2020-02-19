/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

// correct DI for screens require large refactoring, so we just save singleton reference on application
// it's should be set from ios swift code
lateinit var application: BaseApplication