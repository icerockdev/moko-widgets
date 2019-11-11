/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.sample.UsersViewModel
import com.icerockdev.library.units.UserUnitItem
import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.WidgetScope

class SharedFactory {
    val mainWidgetScope by lazy {
        WidgetScope()
    }

    val usersUnitsFactory by lazy {
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
}
