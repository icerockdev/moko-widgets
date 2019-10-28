package dev.icerock.moko.widgets.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Nikita on 28/03/2019.
 */
@BindingAdapter("app:spanCount")
fun setSpanCount(view: RecyclerView, spanCount: Int) {
    view.layoutManager = GridLayoutManager(view.context, spanCount)
}
