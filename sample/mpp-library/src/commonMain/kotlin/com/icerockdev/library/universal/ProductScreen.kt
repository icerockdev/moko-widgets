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
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getArgument
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.listen
import dev.icerock.moko.widgets.text

class ProductScreen : WidgetScreen<Args.Parcel<ProductScreen.Args>>(),
    ProductViewModel.EventsListener {
    override fun createContentWidget(): Widget {
        val arg = getArgument()
        val viewModel = getViewModel {
            ProductViewModel(
                productId = arg.productId,
                eventsDispatcher = createEventsDispatcher()
            )
        }
        viewModel.eventsDispatcher.listen(this, this)

        return with(WidgetScope()) {
            linear(
                childs = listOf(
                    text(text = viewModel.title),
                    button(
                        text = const("Add to Cart"),
                        onTap = viewModel::onCartPressed
                    )
                )
            )
        }
    }

    override fun routeToCart() {
        dispatchNavigation { routeToScreen(CartScreen::class) }
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