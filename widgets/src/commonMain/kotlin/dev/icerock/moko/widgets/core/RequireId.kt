package dev.icerock.moko.widgets.core

interface RequireId<T : WidgetScope.Id> : OptionalId<T> {
    override val id: T
}
