/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.core.Parcelize
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.DrawableResource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.container
import dev.icerock.moko.widgets.core.AnyWidget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.linear
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.text
import kotlin.reflect.KClass

sealed class Args {
    data class Parcel<T : Parcelable>(val args: T) : Args()
    object Empty : Args()
}

expect abstract class Screen<Arg : Args> {
    inline fun <reified VM : ViewModel, Key : Any> getViewModel(key: Key, crossinline viewModelFactory: () -> VM): VM

    fun <T : Any> createEventsDispatcher(): EventsDispatcher<T>

    fun dispatchNavigation(actions: Navigation.() -> Unit)
}

expect abstract class WidgetScreen<Arg : Args>() : Screen<Arg> {
    abstract fun createContentWidget(): AnyWidget
}

inline fun <Arg : Args, reified VM : ViewModel> Screen<Arg>.getViewModel(crossinline viewModelFactory: () -> VM): VM {
    return getViewModel(Unit, viewModelFactory)
}

expect fun <T : Parcelable> Screen<Args.Parcel<T>>.getArgument(): T

interface Navigation {
    fun <S : Screen<Args.Empty>> routeToScreen(screen: KClass<S>)
    fun <Arg : Parcelable, S : Screen<Args.Parcel<Arg>>> routeToScreen(screen: KClass<S>, argument: Arg)
}

expect abstract class BottomNavigationScreen() : Screen<Args.Empty> {
    abstract val items: List<BottomNavigationItem>
}

data class BottomNavigationItem(
    val id: Int,
    val title: StringDesc,
    val icon: DrawableResource? = null,
    val screen: KClass<out Screen<Args.Empty>>
)

expect abstract class NavigationScreen() : Screen<Args.Empty> {
    abstract val rootScreen: KClass<out Screen<Args.Empty>>
}

expect abstract class DrawerNavigationScreen() : Screen<Args.Empty> {
    abstract val header: Any? // what info in header?
    abstract val items: List<DrawerNavigationItem>
    abstract val secondaryItems: List<DrawerNavigationItem>
}

data class DrawerNavigationItem(
    val id: Int,
    val title: StringDesc,
    val icon: DrawableResource? = null,
    val screen: KClass<out Screen<Args.Empty>>
)

expect fun <T : Any> EventsDispatcher<T>.listen(screen: Screen<*>, listener: T)

class ProductScreen : WidgetScreen<Args.Parcel<ProductScreen.Args>>(), ProductViewModel.EventsListener {
    override fun createContentWidget(): AnyWidget {
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
    override fun createContentWidget(): AnyWidget {
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
    override fun createContentWidget(): AnyWidget {
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

// RESERVED FUNCTION
fun getRootScreen(): KClass<out Screen<Args.Empty>> {
    return MyBottomNavigationScreen::class
}
