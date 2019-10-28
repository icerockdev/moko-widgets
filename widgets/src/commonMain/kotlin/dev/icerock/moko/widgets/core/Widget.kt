package dev.icerock.moko.widgets.core

abstract class Widget {
    abstract fun buildView(viewFactoryContext: ViewFactoryContext): View
}