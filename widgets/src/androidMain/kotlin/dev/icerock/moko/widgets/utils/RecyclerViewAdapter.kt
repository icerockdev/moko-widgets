/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:spanCount")
fun setSpanCount(view: RecyclerView, spanCount: Int) {
    view.layoutManager = GridLayoutManager(view.context, spanCount)
}
