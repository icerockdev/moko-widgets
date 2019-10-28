package dev.icerock.moko.widgets

class WidgetScope(internal val properties: MutableMap<Key, Any>) {

    constructor(parent: WidgetScope? = null, builder: Builder.() -> Unit) : this(
        properties = parent?.properties?.toMutableMap() ?: mutableMapOf()
    ) {
        Builder(scope = this).builder()
    }

    interface Key

    class Builder(val scope: WidgetScope)
}