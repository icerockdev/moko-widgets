package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applySizeToChild
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.UILabel
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.backgroundColor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints


import platform.CoreGraphics.CGFloat
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor

actual class SystemModalViewFactory actual constructor(
    private val background: Background?
) : ViewFactory<ContainerWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ContainerWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val view = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            backgroundColor = UIColor.clearColor
            applyBackgroundIfNeeded(background)
        }


        val viewController: UIViewController = viewFactoryContext
        widget.children.forEach { (childWidget, childAlignment) ->
            val childViewBundle = childWidget.buildView(viewController)
            val childView = childViewBundle.view
            childView.translatesAutoresizingMaskIntoConstraints = false

            view.addSubview(childView)


            val edges: Edges<CGFloat> = applySizeToChild(
                rootView = view,
                rootPadding = null,
                childView = childView,
                childSize = childViewBundle.size,
                childMargins = childViewBundle.margins
            )

            when (childAlignment) {
                Alignment.CENTER -> {
                    childView.centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).active =
                        true
                    childView.centerYAnchor.constraintEqualToAnchor(view.centerYAnchor).active =
                        true
                }
                Alignment.LEFT -> {
                    childView.leadingAnchor.constraintEqualToAnchor(
                        anchor = view.leadingAnchor,
                        constant = edges.leading
                    ).active = true
                    childView.centerYAnchor.constraintEqualToAnchor(view.centerYAnchor).active =
                        true
                }
                Alignment.RIGHT -> {
                    childView.trailingAnchor.constraintEqualToAnchor(
                        anchor = view.trailingAnchor,
                        constant = edges.trailing
                    ).active = true
                    childView.centerYAnchor.constraintEqualToAnchor(view.centerYAnchor).active =
                        true
                }
                Alignment.TOP -> {
                    childView.topAnchor.constraintEqualToAnchor(
                        anchor = view.topAnchor,
                        constant = edges.top
                    ).active = true
                    childView.centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).active =
                        true
                }
                Alignment.BOTTOM -> {
                    childView.bottomAnchor.constraintEqualToAnchor(
                        anchor = view.bottomAnchor,
                        constant = edges.bottom
                    ).active = true
                    childView.centerXAnchor.constraintEqualToAnchor(view.centerXAnchor).active =
                        true
                }
            }
        }


        return ViewBundle(
            view = view,
            size = size,
            margins = null
        )
    }
}