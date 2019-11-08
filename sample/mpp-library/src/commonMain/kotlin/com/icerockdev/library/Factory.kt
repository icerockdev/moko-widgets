package com.icerockdev.library

import com.icerockdev.library.sample.UsersViewModel
import com.icerockdev.library.screen.HostScreen
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.WidgetScope

class Factory {
    val mainWidgetScope by lazy {
        WidgetScope()
    }

    private val usersUnitsFactory by lazy {
        object : UsersViewModel.UnitsFactory {
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

    fun createMainScreen(): HostScreen {
        return HostScreen(
            widgetScope = mainWidgetScope,
            cryptoScope = Theme.cryptoWidgetScope,
            social1Scope = mainWidgetScope,
            social2Scope = Theme.socialWidgetScope,
            mcommerceScope = Theme.mcommerceWidgetScope,
            usersUnitsFactory = usersUnitsFactory
        )
    }
}
