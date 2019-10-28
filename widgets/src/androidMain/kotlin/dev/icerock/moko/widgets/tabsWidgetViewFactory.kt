package com.icerockdev.mpp.widgets

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.icerockdev.mpp.widgets.databinding.WidgetTabsBinding

actual var tabsWidgetViewFactory: VFC<TabsWidget> = { context: ViewFactoryContext,
                                                      widget: TabsWidget ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetTabsBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_tabs, parent, false)
    binding.widget = widget
    binding.setLifecycleOwner(lifecycleOwner)
    binding.tabhost.setup()

    widget.tabs.forEachIndexed { index, tabWidget ->
        val tab = binding.tabhost.newTabSpec("tab$index").apply {
            setContent {
                tabWidget.body.buildView(context)
            }
            setIndicator(tabWidget.title.liveData().value.toString(ctx))
        }
        tabWidget.title.liveData().ld().observe(lifecycleOwner, Observer {
            if (it == null) return@Observer
            tab.setIndicator(it.toString(ctx))
        })
        binding.tabhost.addTab(tab)
    }

    binding.root
}
