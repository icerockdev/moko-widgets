/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.icerock.moko.graphics.Color
import dev.icerock.moko.widgets.core.widget.TabsWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.style.applyPaddingIfNeeded
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.state.SelectableState
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.ThemeAttrs

actual class SystemTabsViewFactory actual constructor(
    private val tabsTintColor: Color?,
    private val titleColor: SelectableState<Color?>?,
    private val tabsBackground: Background<Fill.Solid>?,
    private val contentBackground: Background<out Fill>?,
    private val tabsPadding: PaddingValues?,
    private val contentPadding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<TabsWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: TabsWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val lifecycleOwner = viewFactoryContext.lifecycleOwner

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL

            applyBackgroundIfNeeded(this@SystemTabsViewFactory.contentBackground)
        }

        val tabLayout = TabLayout(context).apply {
            id = android.R.id.tabs

            applyBackgroundIfNeeded(this@SystemTabsViewFactory.tabsBackground)
        }
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //no need
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //no need
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                widget.onTabSelected?.let { it(tab?.position ?: 0) }
            }

        })

        container.addView(
            tabLayout,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        tabsPadding?.also {
            val mlp = tabLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.topMargin = it.top.toInt()
            mlp.bottomMargin = it.bottom.toInt()
            mlp.leftMargin = it.start.toInt()
            mlp.rightMargin = it.end.toInt()
        }

        val viewPagerAdapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return widget.tabs.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val viewBundle = widget.tabs[position].body.buildView(
                    ViewFactoryContext(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        parent = container
                    )
                )
                val view = viewBundle.view
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View?)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return widget.tabs[position].title.value.toString(context)
            }
        }
        val viewPager = ViewPager(context).apply {
            id = android.R.id.tabcontent

            applyPaddingIfNeeded(contentPadding)


            adapter = viewPagerAdapter
        }

        container.addView(
            viewPager,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        tabsTintColor?.also {
            tabLayout.setSelectedTabIndicatorColor(it.argb.toInt())
        }
        titleColor?.also { stateColor ->
            tabLayout.tabTextColors = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf(-android.R.attr.state_selected)
                ),
                intArrayOf(
                    stateColor.selected?.argb?.toInt() ?: ThemeAttrs.getTextColorPrimary(context),
                    stateColor.unselected?.argb?.toInt() ?: ThemeAttrs.getTextColorSecondary(context)
                )
            )
        }

        tabLayout.setupWithViewPager(viewPager)

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }
}
