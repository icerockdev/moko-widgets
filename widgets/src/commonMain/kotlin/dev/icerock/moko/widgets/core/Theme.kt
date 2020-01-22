/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.asLiveData

class Theme(
    private val properties: MutableMap<Any, Any>
) {
    constructor() : this(
        properties = mutableMapOf()
    )

    constructor(parent: Theme? = null, builder: Builder.() -> Unit) : this(
        properties = parent?.properties?.deepMutableCopy() ?: mutableMapOf()
    ) {
        Builder(theme = this).builder()
    }

    fun const(value: String): LiveData<StringDesc> {
        return value.desc().asLiveData()
    }

    fun <T> const(value: T): LiveData<T> {
        return value.asLiveData()
    }

    interface Id<T : Widget<out WidgetSize>>
    interface Category<T : Widget<out WidgetSize>>

    interface ReadFactory {
        operator fun <T : Widget<out WidgetSize>> get(id: Id<T>): ViewFactory<T>?
        operator fun <T : Widget<out WidgetSize>> get(clazz: Category<T>): ViewFactory<T>?

        fun <T : Widget<out WidgetSize>> get(
            id: Id<T>?,
            category: Category<T>?,
            defaultCategory: Category<T>,
            fallback: () -> ViewFactory<T>
        ): ViewFactory<T> {
            return id?.let { get(it) } ?: category?.let { get(it) } ?: get(defaultCategory) ?: fallback()
        }
    }

    interface ReadWriteFactory : ReadFactory {
        operator fun <T : Widget<out WidgetSize>> set(id: Id<T>, factory: ViewFactory<T>)
        operator fun <T : Widget<out WidgetSize>> set(clazz: Category<T>, factory: ViewFactory<T>)
    }

    private inner class ReadFactoryImpl : ReadFactory {
        override operator fun <T : Widget<out WidgetSize>> get(id: Id<T>): ViewFactory<T>? {
            return properties[id] as? ViewFactory<T>
        }

        override operator fun <T : Widget<out WidgetSize>> get(clazz: Category<T>): ViewFactory<T>? {
            return properties[clazz] as? ViewFactory<T>
        }
    }

    val factory: ReadFactory = ReadFactoryImpl()

    class Builder(val theme: Theme) {
        private inner class ReadWriteFactoryImpl : ReadWriteFactory, ReadFactory by theme.factory {
            override fun <T : Widget<out WidgetSize>> set(id: Id<T>, factory: ViewFactory<T>) {
                theme.properties[id] = factory
            }

            override fun <T : Widget<out WidgetSize>> set(clazz: Category<T>, factory: ViewFactory<T>) {
                theme.properties[clazz] = factory
            }
        }

        val factory: ReadWriteFactory = ReadWriteFactoryImpl()
    }
}

private fun <K, V> Map<K, V>.deepMutableCopy(): MutableMap<K, V> = mutableMapOf<K, V>().also {
    forEach { (key, value) ->
        it[key] = value
    }
}
