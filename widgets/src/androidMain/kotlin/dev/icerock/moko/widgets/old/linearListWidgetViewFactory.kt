/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var linearListWidgetViewFactory: VFC<LinearListWidget> = { context: ViewFactoryContext,
                                                                  widget: LinearListWidget ->
    TODO()
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(context.context)
//    val binding: WidgetLinearListBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_linear_list, parent, false)
//
//
//    binding.apply {
//
//        widget.style.paddingValues.apply {
//            recyclerView.clipToPadding = false
//            recyclerView.setDpPadding(
//                resources = context.context.resources,
//                paddingStart = start,
//                paddingEnd = end,
//                paddingTop = top,
//                paddingBottom = bottom
//            )
//        }
//    }
//
//    (binding.recyclerView.layoutManager as? LinearLayoutManager)?.run {
//        orientation = when (widget.style.orientation) {
//            LinearListWidget.Orientation.VERTICAL -> RecyclerView.VERTICAL
//            LinearListWidget.Orientation.HORIZONTAL -> RecyclerView.HORIZONTAL
//        }
//        reverseLayout = widget.style.reversed
//    }
//
//    binding.lifecycleOwner = context.lifecycleOwner
//    binding.widget = widget
//
//    val onReachEnd = widget.onReachEnd
//    val sourceLiveData = widget.source.liveData()
//
//    bindList(
//        onReachEnd = onReachEnd,
//        liveData = sourceLiveData
//    ) { binding.list = it.ld() }
//    binding.root
}
//
//internal fun bindList(
//    onReachEnd: (() -> Unit)?,
//    liveData: LiveData<List<UnitItem>>,
//    bindingSetter: (LiveData<List<UnitItem>>) -> Unit
//) {
//    if (onReachEnd != null) {
//        val listLd = liveData.map {
//            if (it.isEmpty()) it
//            else it.subList(0, it.lastIndex) + BindingClassDecorator(it.last()) {
//                onReachEnd()
//            }
//        }
//        bindingSetter(listLd)
//    } else {
//        bindingSetter(liveData)
//    }
//}
//
//class BindingClassDecorator(
//    val decorated: UnitItem,
//    val onBind: () -> Unit
//) : UnitItem {
//    init {
//        layoutParams = decorated.layoutParams
//    }
//
//    override val itemId: Long = decorated.itemId
//
//    override val layoutId: Int = decorated.layoutId
//
//    override fun bind(viewDataBinding: ViewDataBinding) {
//        decorated.bind(viewDataBinding)
//        onBind()
//    }
//}
