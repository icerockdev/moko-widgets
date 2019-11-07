/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.view

import android.content.Context

internal class AspectRatioFrameLayout(
    context: Context,
    val aspectRatio: Float,
    val aspectByWidth: Boolean
) : MarginedFrameLayout(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val (width: Int, height: Int) = if (aspectByWidth) {
            measuredWidth to (measuredWidth / aspectRatio).toInt()
        } else {
            (measuredHeight * aspectRatio).toInt() to measuredHeight
        }

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}