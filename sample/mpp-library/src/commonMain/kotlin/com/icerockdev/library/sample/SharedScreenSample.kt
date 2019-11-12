/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.sample

import com.icerockdev.library.SharedFactory
import com.icerockdev.library.Theme
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.TabsWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BottomNavigationItem
import dev.icerock.moko.widgets.screen.BottomNavigationScreen
import dev.icerock.moko.widgets.screen.NavigationScreen
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getArgument
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.listen
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.tabs
import dev.icerock.moko.widgets.text
import kotlin.reflect.KClass

class ProductScreen : WidgetScreen<Args.Parcel<ProductScreen.Args>>(), ProductViewModel.EventsListener {
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

class CartScreen : WidgetScreen<Args.Empty>() {
    override fun createContentWidget(): Widget {
        return with(WidgetScope()) {
            container(
                styled = { it.copy(size = WidgetSize.Const(SizeSpec.AS_PARENT, SizeSpec.AS_PARENT)) },
                childs = mapOf(
                    text(text = const("cart")) to Alignment.CENTER
                )
            )
        }
    }
}

class ProductsScreen : WidgetScreen<Args.Empty>() {
    override fun createContentWidget(): Widget {
        return with(WidgetScope()) {
            container(
                styled = { it.copy(size = WidgetSize.Const(SizeSpec.AS_PARENT, SizeSpec.AS_PARENT)) },
                childs = mapOf(
                    button(
                        text = const("go to product"),
                        onTap = ::onProductPressed
                    ) to Alignment.CENTER
                )
            )
        }
    }

    private fun onProductPressed() {
        println("go to product!")
        dispatchNavigation { routeToScreen(ProductScreen::class, ProductScreen.Args(10)) }
    }
}

class MyBottomNavigationScreen : BottomNavigationScreen() {
    override val items: List<BottomNavigationItem> = listOf(
        BottomNavigationItem(
            id = 1,
            title = "Products".desc(),
            screen = ProductsNavigationScreen::class
        ),
        BottomNavigationItem(
            id = 2,
            title = "Cart".desc(),
            screen = CartNavigationScreen::class
        ),
        BottomNavigationItem(
            id = 3,
            title = "Widgets".desc(),
            screen = WidgetsScreen::class
        )
    )
}

class ProductsNavigationScreen : NavigationScreen() {
    override val rootScreen: KClass<out Screen<Args.Empty>>
        get() = ProductsScreen::class
}

class CartNavigationScreen : NavigationScreen() {
    override val rootScreen: KClass<out Screen<Args.Empty>>
        get() = CartScreen::class
}

//fun KClass<out Screen<Args.Empty>>.withNavigation(): KClass<out NavigationScreen> {
//    val screen = this
//    return object : NavigationScreen() {
//        override val rootScreen: KClass<out Screen<Args.Empty>> get() = screen
//    }::class
//}

class WidgetsScreen : WidgetScreen<Args.Empty>() {
    private val sharedFactory = SharedFactory() // TODO change system

    override fun createContentWidget(): Widget {
        return with(WidgetScope()) {
            tabs(
                tabs = listOf(
                    TabsWidget.TabWidget(
                        title = const("P#2"),
                        body = SocialProfileScreen(
                            widgetScope = Theme.socialWidgetScope,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#4"),
                        body = CryptoProfileScreen(
                            widgetScope = Theme.cryptoWidgetScope,
                            viewModel = CryptoProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#1"),
                        body = SocialProfileScreen(
                            widgetScope = this,
                            viewModel = SocialProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P#3"),
                        body = McommerceProfileScreen(
                            widgetScope = Theme.mcommerceWidgetScope,
                            viewModel = McommerceProfileViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("D"),
                        body = StateScreen(
                            widgetScope = this,
                            viewModel = StateViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("P"),
                        body = PostsScreen(
                            widgetScope = this,
                            viewModel = PostsViewModel()
                        ).createWidget()
                    ),
                    TabsWidget.TabWidget(
                        title = const("U"),
                        body = UsersScreen(
                            widgetScope = this,
                            viewModel = UsersViewModel(sharedFactory.usersUnitsFactory)
                        ).createWidget()
                    )
                )
            )
        }
    }
}
