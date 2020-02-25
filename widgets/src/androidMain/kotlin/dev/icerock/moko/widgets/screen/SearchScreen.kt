/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.cardview.widget.CardView
import dev.icerock.moko.graphics.colorInt
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.list
import dev.icerock.moko.widgets.style.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.WidgetSize


actual abstract class SearchScreen<A : Args> : Screen<A>() {
    actual abstract val searchPlaceholder: StringDesc
    actual abstract val searchQuery: MutableLiveData<String>
    actual abstract val searchItems: LiveData<List<TableUnitItem>>
    actual open val background: Background<*>? = null
    actual open val androidBackItem: Image? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {

        val rootLayout = LinearLayout(context)
        rootLayout.orientation = LinearLayout.VERTICAL

        context?.let { context ->
            val cardView = CardView(context)


            val searchContainer = LinearLayout(context)
            searchContainer.orientation = LinearLayout.HORIZONTAL
            searchContainer.gravity = Gravity.CENTER_VERTICAL
            searchContainer.setVerticalGravity(Gravity.CENTER_VERTICAL)
            androidStatusBarColor?.let {
                searchContainer.setBackgroundColor(it.colorInt())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    searchContainer.elevation = 10f
                }
            }
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                val actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    resources.displayMetrics
                )
                searchContainer.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    actionBarHeight
                )
            }

            if (androidBackItem != null) {
                val imageView = ImageView(context)
                imageView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    val margin = (20 * context.resources.displayMetrics.density).toInt()
                    setMargins(margin, 0, margin, 0)
                }
                androidBackItem?.loadIn(imageView)
                imageView.setOnClickListener {
                    activity?.onBackPressed()
                }
                searchContainer.addView(imageView)
            }

            val radius = 8 * context.resources.displayMetrics.density
            val padding = radius.toInt()
            cardView.radius = radius
            cardView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(padding, padding, padding, padding)
            }

            val searchView = SearchView(context)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery.value = query ?: ""
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.isEmpty() == true) {
                        searchQuery.value = newText
                        return true
                    }
                    return false
                }

            })
            searchView.setOnSearchClickListener { _ ->
                searchQuery.value = searchView.query.toString()
            }
            searchView.queryHint = searchPlaceholder.toString(context)

            searchView.setBackgroundColor(Color.WHITE)

            cardView.addView(searchView)

            searchContainer.addView(cardView)

            rootLayout.addView(searchContainer)
        }

        val listWidget =
            Theme().list(
                size = WidgetSize.AsParent,
                id = Ids.ListId,
                items = searchItems
            )

        val view = listWidget.buildView(
            ViewFactoryContext(
                context = requireContext(),
                lifecycleOwner = this,
                parent = container
            )
        ).view

        rootLayout.applyBackgroundIfNeeded(background)
        rootLayout.addView(view)
        return rootLayout
    }

    object Ids {
        object ListId : ListWidget.Id
    }
}
