package com.icerockdev.mpp.widgets

expect var centerWidgetViewFactory: VFC<CenterWidget>

class CenterWidget(
    private val factory: VFC<CenterWidget>,
    val child: Widget
) : Widget() {

    constructor(
        factory: VFC<CenterWidget>,
        childFactory: () -> Widget
    ) : this(
        factory = factory,
        child = childFactory()
    )

    override fun buildView(viewFactoryContext: ViewFactoryContext): View =
        factory(viewFactoryContext, this)
}
