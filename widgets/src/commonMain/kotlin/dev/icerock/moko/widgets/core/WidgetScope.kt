/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WidgetScope(private val properties: MutableMap<Key, Any>) {

    constructor() : this(properties = mutableMapOf<Key, Any>())

    constructor(parent: WidgetScope? = null, builder: Builder.() -> Unit) : this(
        properties = parent?.properties?.deepMutableCopy() ?: mutableMapOf()
    ) {
        Builder(scope = this).builder()
    }

    fun childScope(builder: Builder.() -> Unit): WidgetScope {
        return WidgetScope(parent = this, builder = builder)
    }

    fun const(value: String): LiveData<StringDesc> {
        return value.desc().asLiveData()
    }

    fun <T> const(value: T): LiveData<T> {
        return value.asLiveData()
    }

    interface Key

    interface Id

    class Builder(val scope: WidgetScope)

    companion object {
        internal inline fun <reified T : Any> readProperty(key: Key, crossinline fallback: () -> T) =
            object : ReadOnlyProperty<WidgetScope, T> {
                override fun getValue(thisRef: WidgetScope, property: KProperty<*>): T {
                    val factory = thisRef.properties[key] as? T
                    return (factory ?: fallback())
                }
            }

        internal inline fun <reified T : Any> readWriteProperty(
            key: Key,
            crossinline readProperty: WidgetScope.() -> T
        ) = object : ReadWriteProperty<Builder, T> {
            override fun setValue(thisRef: Builder, property: KProperty<*>, value: T) {
                thisRef.scope.properties[key] = value
            }

            override fun getValue(thisRef: Builder, property: KProperty<*>): T {
                return thisRef.scope.readProperty()
            }
        }
    }
}

private fun <K, V> Map<K, V>.deepMutableCopy(): MutableMap<K, V> = mutableMapOf<K, V>().also {
    forEach { (key, value) ->
        it[key] = value
    }
}

fun buildWidget(scope: WidgetScope, builder: WidgetScope.() -> Widget): Widget {
    return scope.builder()
}
