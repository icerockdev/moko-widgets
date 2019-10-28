package dev.icerock.moko.widgets

abstract class Widget {
    abstract fun buildView(viewFactoryContext: ViewFactoryContext): View
}