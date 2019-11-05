package com.icerockdev.library

import com.icerockdev.library.screen.MainScreen
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.ViewModelProvider
import dev.icerock.moko.widgets.core.WidgetScope

class Factory {
    val mainWidgetScope by lazy {
        WidgetScope()
    }

    private val unitsFactory by lazy {
        object : MainViewModel.UnitsFactory {
            override fun createUserUnit(
                itemId: Long,
                name: String,
                avatarUrl: String,
                onClick: () -> Unit
            ): UnitItem = UserUnitItem(
                widgetScope = mainWidgetScope,
                itemId = itemId,
                data = UserUnitItem.Data(
                    name = name,
                    avatarUrl = avatarUrl,
                    onClick = onClick
                )
            )
        }
    }

    fun createMainViewModel(title: String): MainViewModel {
        return MainViewModel(
            title = title,
            unitsFactory = unitsFactory
        )
    }

    private val mainViewModelProvider: ViewModelProvider<MainViewModel, MainScreen.Args> by lazy {
        object : ViewModelProvider<MainViewModel, MainScreen.Args> {
            override fun createViewModel(arguments: MainScreen.Args): MainViewModel {
                return createMainViewModel(title = arguments.title)
            }
        }
    }

    fun createMainScreen(): MainScreen {
        return MainScreen(
            widgetScope = mainWidgetScope,
            cryptoScope = Theme.cryptoWidgetScope,
            social1Scope = mainWidgetScope,
            social2Scope = Theme.socialWidgetScope,
            mcommerceScope = Theme.mcommerceWidgetScope,
            viewModelProvider = mainViewModelProvider
        )
    }
}
