package dev.icerock.moko.widgets.core

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WidgetScope(internal val properties: MutableMap<Key, Any>) {

    constructor() : this(properties = mutableMapOf<Key, Any>())

    constructor(parent: WidgetScope? = null, builder: Builder.() -> Unit) : this(
        properties = parent?.properties?.deepMutableCopy() ?: mutableMapOf()
    ) {
        Builder(scope = this).builder()
    }

    interface Key

    class Builder(val scope: WidgetScope)

    companion object {
        internal inline fun <reified T : Any> readProperty(key: Key, crossinline fallback: () -> T) =
            object : ReadOnlyProperty<WidgetScope, T> {
                override fun getValue(thisRef: WidgetScope, property: KProperty<*>): T {
                    val factory = thisRef.properties[key] as? T
                    return (factory ?: fallback())
                }
            }

        internal inline fun <reified T : Any> readWriteProperty(key: Key, readProperty: KProperty<T>) =
            object : ReadWriteProperty<Builder, T> {
                override fun setValue(thisRef: Builder, property: KProperty<*>, value: T) {
                    thisRef.scope.properties[key] = value
                }

                override fun getValue(thisRef: Builder, property: KProperty<*>): T {
                    return readProperty.call(thisRef.scope)
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

fun WidgetScope.childScope(builder: WidgetScope.Builder.() -> Unit): WidgetScope {
    return WidgetScope(parent = this, builder = builder)
}
