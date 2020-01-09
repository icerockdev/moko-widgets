/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style.view

sealed class SizeSpec {
    object AsParent : SizeSpec()
    object WrapContent : SizeSpec()
    data class Exact(val points: Float) : SizeSpec()
    object MatchConstraint: SizeSpec()
}
