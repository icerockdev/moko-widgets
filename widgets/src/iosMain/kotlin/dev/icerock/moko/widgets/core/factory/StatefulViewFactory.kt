/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.mvvm.ResourceState
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.style.background.Background
import dev.icerock.moko.widgets.core.style.background.Fill
import dev.icerock.moko.widgets.core.style.view.MarginValues
import dev.icerock.moko.widgets.core.style.view.PaddingValues
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.utils.Edges
import dev.icerock.moko.widgets.core.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.core.utils.applySizeToChild
import dev.icerock.moko.widgets.core.utils.fillChildView
import dev.icerock.moko.widgets.core.widget.StatefulWidget
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.hidden
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

@ExportObjCClass
private class StatefulViewContainer : UIView(frame = CGRectZero.readValue())

actual class StatefulViewFactory actual constructor(
    private val margins: MarginValues?,
    private val padding: PaddingValues?,
    private val background: Background<out Fill>?
) : ViewFactory<StatefulWidget<out WidgetSize, *, *>> {

    override fun <WS : WidgetSize> build(
        widget: StatefulWidget<out WidgetSize, *, *>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val container = StatefulViewContainer().apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyBackgroundIfNeeded(background)
        }

        val widgetStateMap: Map<Widget<*>, (ResourceState<*, *>) -> Boolean> = mapOf(
            widget.dataWidget to { it is ResourceState.Success },
            widget.emptyWidget to { it is ResourceState.Empty },
            widget.loadingWidget to { it is ResourceState.Loading },
            widget.errorWidget to { it is ResourceState.Failed }
        )

        widgetStateMap.forEach { (childWidget, isRelatedState) ->
            val childViewBundle = childWidget.buildView(viewController)
            val childView = childViewBundle.view
            childView.translatesAutoresizingMaskIntoConstraints = false

            with(container) {
                addSubview(childView)

                val edges: Edges<CGFloat> =
                    applySizeToChild(
                        rootPadding = padding,
                        rootView = container,
                        childView = childView,
                        childSize = childViewBundle.size,
                        childMargins = childViewBundle.margins
                    )

                fillChildView(childView, edges)
            }

            fun updateState(state: ResourceState<*, *>) {
                childView.hidden = isRelatedState(state).not()
            }

            updateState(widget.state.value)
            widget.state.addObserver { updateState(it) }
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }
}
