package dev.icerock.moko.widgets

import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var tabsWidgetViewFactory: VFC<TabsWidget> = { context: ViewFactoryContext,
                                                      widget: TabsWidget ->
    val ctx = context.context
    val lifecycleOwner = context.lifecycleOwner

    val tabHost = TabHost(ctx).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        id = android.R.id.tabhost
    }

    val container = LinearLayout(ctx).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        orientation = LinearLayout.VERTICAL
    }

    tabHost.addView(container)

    val tabsScroll = HorizontalScrollView(ctx).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }

    val tabWidget = TabWidget(ctx).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        id = android.R.id.tabs
    }

    tabsScroll.addView(tabWidget)
    container.addView(tabsScroll)

    val content = FrameLayout(ctx).apply {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        id = android.R.id.tabcontent
    }
    container.addView(content)

    tabHost.setup()

    widget.tabs.forEachIndexed { index, tab ->
        fun createIndicator(stringDesc: StringDesc): View {
            val tabContainer = FrameLayout(
                if (tabHost.isInEditMode) ctx else ContextThemeWrapper(
                    ctx,
                    android.R.style.Widget_Material_Tab
                )
            )
            val text = TextView(
                if (tabHost.isInEditMode) ctx else ContextThemeWrapper(
                    ctx,
                    android.R.style.Widget_Material_ActionBar_TabText
                )
            ).apply {
                text = stringDesc.toString(ctx)
            }
            tabContainer.addView(text)
            return tabContainer
        }

        val tabSpec = tabHost.newTabSpec("tab$index").apply {
            setContent {
                tab.body.buildView(
                    ViewFactoryContext(
                        context = ctx,
                        parent = content,
                        lifecycleOwner = lifecycleOwner
                    )
                )
            }
            setIndicator(createIndicator(tab.title.value))
        }
        tab.title.ld().observe(lifecycleOwner, Observer {
            tabSpec.setIndicator(createIndicator(it))
        })
        tabHost.addTab(tabSpec)
    }

    tabHost
}
