package dev.icerock.moko.widgets.core

interface RequireId<T : Theme.Id> : OptionalId<T> {
    override val id: T
}
