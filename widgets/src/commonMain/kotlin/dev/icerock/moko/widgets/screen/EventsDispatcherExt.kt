/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher

expect fun <T : Any> EventsDispatcher<T>.listen(screen: Screen<*>, listener: T)
