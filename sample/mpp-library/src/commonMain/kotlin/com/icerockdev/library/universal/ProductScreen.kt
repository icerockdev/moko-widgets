package com.icerockdev.library.universal

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getArgument
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.listen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.navigation.Route
import dev.icerock.moko.widgets.screen.navigation.route
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text

class ProductScreen(
    private val theme: Theme,
    private val cartRoute: Route<Unit>
) : WidgetScreen<Args.Parcel<ProductScreen.Args>>(),
    ProductViewModel.EventsListener, NavigationItem {
    override val navigationBar
        get() = NavigationBar.Normal(title = getArgument().productId.let { "Product $it".desc() })

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val arg = getArgument()
        val viewModel = getViewModel {
            ProductViewModel(
                productId = arg.productId,
                eventsDispatcher = createEventsDispatcher()
            )
        }
        viewModel.eventsDispatcher.listen(this, this)

        return with(theme) {
            container(size = WidgetSize.AsParent) {
                center {
                    linear(size = WidgetSize.WrapContent) {
                        +text(
                            size = WidgetSize.WrapContent,
                            text = viewModel.title
                        )
                        +button(
                            size = WidgetSize.WrapContent,
                            content = ButtonWidget.Content.Text(Value.data("Add to cart".desc())),
                            onTap = viewModel::onCartPressed
                        )
                    }
                }
            }
        }
    }

    override fun routeToCart() {
        cartRoute.route(this)
    }

    @Parcelize
    data class Args(val productId: Int) : Parcelable
}

class ProductViewModel(
    val productId: Int,
    override val eventsDispatcher: EventsDispatcher<EventsListener>
) : ViewModel(), EventsDispatcherOwner<ProductViewModel.EventsListener> {
    val title: LiveData<StringDesc> = MutableLiveData(initialValue = "my product $productId".desc())

    fun onCartPressed() {
        eventsDispatcher.dispatchEvent { routeToCart() }
    }

    interface EventsListener {
        fun routeToCart()
    }
}
