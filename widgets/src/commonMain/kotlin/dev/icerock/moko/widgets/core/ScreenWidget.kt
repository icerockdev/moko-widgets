package dev.icerock.moko.widgets

abstract class ScreenWidget : Widget() {
    val body: Widget by lazy { build() }

    abstract fun build(): Widget

    override fun buildView(viewFactoryContext: ViewFactoryContext): View = body.buildView(viewFactoryContext)
}
