/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

@Suppress("FunctionParameterNaming")
expect fun getAssociatedObject(`object`: kotlin.Any?): kotlin.Any?

@Suppress("FunctionParameterNaming")
expect fun setAssociatedObject(`object`: kotlin.Any?, value: kotlin.Any?)

expect fun cgColors(uiColors: kotlin.collections.List<*>?): kotlin.collections.List<*>?
