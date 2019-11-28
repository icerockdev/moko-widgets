/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.sample.UsersViewModel
import com.icerockdev.library.units.LoadingUnitWidget
import com.icerockdev.library.units.UserUnitWidget
import dev.icerock.moko.units.CollectionUnitItem
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.widgets.core.Theme

class SharedFactory {
    val mainWidgetScope by lazy {
        Theme()
    }

    val usersUnitsFactory by lazy {
        object : UsersViewModel.UnitsFactory {
            override fun createUserTableUnit(
                itemId: Long,
                name: String,
                avatarUrl: String,
                onClick: () -> Unit
            ): TableUnitItem = UserUnitWidget.TableUnitItem(
                theme = mainWidgetScope,
                itemId = itemId,
                data = UserUnitWidget.Data(
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
            ): CollectionUnitItem = UserUnitWidget.CollectionUnitItem(
                theme = mainWidgetScope,
                itemId = itemId,
                data = UserUnitWidget.Data(
                    name = name,
                    avatarUrl = avatarUrl,
                    onClick = onClick
                )
            )

            override fun createLoadingTableUnit(
                itemId: Long
            ): TableUnitItem = LoadingUnitWidget.TableUnitItem(
                theme = mainWidgetScope,
                itemId = itemId
            )

            override fun createLoadingCollectionUnit(
                itemId: Long
            ): CollectionUnitItem = LoadingUnitWidget.CollectionUnitItem(
                theme = mainWidgetScope,
                itemId = itemId
            )
        }
    }
}
