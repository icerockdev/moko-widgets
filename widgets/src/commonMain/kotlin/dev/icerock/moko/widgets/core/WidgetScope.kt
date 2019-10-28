package dev.icerock.moko.widgets.core

class WidgetScope(internal val properties: MutableMap<Key, Any>) {

    constructor() : this(properties = mutableMapOf<Key, Any>())

    constructor(parent: WidgetScope? = null, builder: Builder.() -> Unit) : this(
        properties = parent?.properties?.toMutableMap() ?: mutableMapOf()
    ) {
        Builder(scope = this).builder()
    }

    interface Key

    class Builder(val scope: WidgetScope)
}

fun buildWidget(scope: WidgetScope, builder: WidgetScope.() -> Widget): Widget {
    return scope.builder()
}

fun WidgetScope.childScope(builder: WidgetScope.Builder.() -> Unit): WidgetScope {
    return WidgetScope(parent = this, builder = builder)
}
