package dev.icerock.moko.widgets.screen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.cardview.widget.CardView
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.ListWidget
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.list
import dev.icerock.moko.widgets.style.view.WidgetSize

actual abstract class SearchScreen<A : Args> actual constructor() : Screen<A>() {

    actual abstract val searchQuery: MutableLiveData<String>
    actual abstract val searchItems: LiveData<List<TableUnitItem>>
    actual open val theme: Theme? = null
    actual open val listCategory: ListWidget.Category? = null
    actual abstract fun onReachEnd()
    open val searchHint: StringDesc? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {

        val rootLayout = LinearLayout(context)
        rootLayout.orientation = LinearLayout.VERTICAL

        context?.let { context ->

            val cardView = CardView(context)

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
            searchView.queryHint = searchHint?.toString(context)

            searchView.setBackgroundColor(Color.WHITE)

            cardView.addView(searchView)

            rootLayout.addView(cardView)
        }

        val listWidget =
            (theme ?: Theme()).list(
                size = WidgetSize.AsParent,
                id = Ids.ListId,
                category = listCategory ?: ListWidget.DefaultCategory,
                items = searchItems,
                onReachEnd = ::onReachEnd
            )

        val view = listWidget.buildView(
            ViewFactoryContext(
                context = requireContext(),
                lifecycleOwner = this,
                parent = container
            )
        ).view

        rootLayout.background = view.background
        view.background = null

        rootLayout.addView(view)
        return rootLayout
    }

    object Ids {
        object ListId : ListWidget.Id
    }
}
