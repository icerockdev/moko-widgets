/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import dev.icerock.moko.units.UnitItem

internal class UnitItemDecorator(
    private val decorated: UnitItem,
    val onBind: () -> Unit
) : UnitItem {

//    init {
//        layoutParams = decorated.layoutParams
//    }

    override val itemId: Long get() = decorated.itemId

    override val viewType: Int get() = decorated.viewType

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        decorated.bindViewHolder(viewHolder)
        onBind()
    }

    override fun createViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder {
        return decorated.createViewHolder(parent, lifecycleOwner)
    }
}
