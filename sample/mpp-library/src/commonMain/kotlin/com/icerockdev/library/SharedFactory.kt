/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.sample.UsersViewModel
import com.icerockdev.library.units.UserUnitItem
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetScope
import dev.icerock.moko.widgets.units.WidgetsCollectionUnitItem

class SharedFactory {
    val mainWidgetScope by lazy {
        WidgetScope()
    }

    val usersUnitsFactory by lazy {
        object : UsersViewModel.UnitsFactory {
            override fun createUserTableUnit(
                itemId: Long,
                name: String,
                avatarUrl: String,
                onClick: () -> Unit
            ): TableUnitItem = UserUnitItem(
                widgetScope = mainWidgetScope,
                itemId = itemId,
                data = UserUnitItem.Data(
                    name = name,
                    avatarUrl = avatarUrl,
                    onClick = onClick
                )
            )

            override fun createUserCollectionUnit(
                itemId: Long,
                name: String,
                avatarUrl: String,
                onClick: () -> Unit
            ): CollectionUnitItem = object : WidgetsCollectionUnitItem<Int>(itemId, 1) {
                override val reuseId: String
                    get() = "user"

                override fun createWidget(data: LiveData<Int>): Widget {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }
    }
}
