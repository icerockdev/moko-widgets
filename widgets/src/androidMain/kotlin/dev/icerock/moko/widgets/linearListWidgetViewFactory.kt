package com.icerockdev.mpp.widgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icerockdev.mpp.binderadapter.generate.BindingClass
import com.icerockdev.mpp.mvvm.livedata.LiveData
import com.icerockdev.mpp.mvvm.livedata.map
import com.icerockdev.mpp.widgets.databinding.WidgetLinearListBinding
import com.icerockdev.mpp.widgets.style.ext.setDpPadding

actual var linearListWidgetViewFactory: VFC<LinearListWidget> = { context: ViewFactoryContext,
                                                                  widget: LinearListWidget ->
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(context.context)
    val binding: WidgetLinearListBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_linear_list, parent, false)


    binding.apply {

        widget.style.paddingValues.apply {
            recyclerView.clipToPadding = false
            recyclerView.setDpPadding(
                resources = context.context.resources,
                paddingStart = start,
                paddingEnd = end,
                paddingTop = top,
                paddingBottom = bottom
            )
        }
    }

    (binding.recyclerView.layoutManager as? LinearLayoutManager)?.run {
        orientation = when (widget.style.orientation) {
            LinearListWidget.Orientation.VERTICAL -> RecyclerView.VERTICAL
            LinearListWidget.Orientation.HORIZONTAL -> RecyclerView.HORIZONTAL
        }
        reverseLayout = widget.style.reversed
    }

    binding.lifecycleOwner = context.lifecycleOwner
    binding.widget = widget

    val onReachEnd = widget.onReachEnd
    val sourceLiveData = widget.source.liveData()

    bindList(
        onReachEnd = onReachEnd,
        liveData = sourceLiveData
    ) { binding.list = it.ld() }
    binding.root
}

internal fun bindList(
    onReachEnd: (() -> Unit)?,
    liveData: LiveData<List<BindingClass>>,
    bindingSetter: (LiveData<List<BindingClass>>) -> Unit
) {
    if (onReachEnd != null) {
        val listLd = liveData.map {
            if (it.isEmpty()) it
            else it.subList(0, it.lastIndex) + BindingClassDecorator(it.last()) {
                onReachEnd()
            }
        }
        bindingSetter(listLd)
    } else {
        bindingSetter(liveData)
    }
}

class BindingClassDecorator(
    val decorated: BindingClass,
    val onBind: () -> Unit
) : BindingClass() {
    init {
        itemId = decorated.itemId
        layoutParams = decorated.layoutParams
    }

    override val layoutId: Int = decorated.layoutId

    override fun bind(viewDataBinding: ViewDataBinding) {
        decorated.bind(viewDataBinding)
        onBind()
    }
}
