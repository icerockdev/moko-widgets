package dev.icerock.moko.widgets.core

interface Styled<T : Any> {
    // TODO change to livedata? change visual by business logic may be required
    val style: T
}