/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.utils

import dev.icerock.moko.widgets.core.BuildConfig
import dev.icerock.moko.widgets.core.StringId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import kotlin.math.abs

private val classIdMap: MutableMap<Theme.Id<*>, Int> = mutableMapOf()

@Suppress("MagicNumber")
val <T : Widget<out WidgetSize>> Theme.Id<T>.androidId: Int
    get() {
        val cachedId = classIdMap[this]
        if (cachedId != null) return cachedId

        val idString: String = if (this is StringId) this.uniqueId
        else this.javaClass.name

        val hashCode = abs(idString.hashCode())
        val id = hashCode % 0x9000
        // 0x7F - application resources package
        // 0x08 - id resource
        // 0x1000 - ids is libraries reserved
        val fullId = 0x7f081000 + id

        if (BuildConfig.DEBUG) {
            println(String.format("id %s transformed to 0x%X", idString, fullId))
        }

        if (classIdMap.containsValue(fullId)) {
            val conflictName: String = classIdMap
                .filter { it.value == fullId }
                .keys.toList()
                .first()
                .javaClass.name

            val msg = String.format(
                "id 0x%X already used by %s, it conflict with %s",
                fullId,
                conflictName,
                idString
            )
            throw AndroidIdConflictException(msg)
        }

        classIdMap[this] = fullId

        return fullId
    }

private class AndroidIdConflictException(message: String) : RuntimeException(message)
