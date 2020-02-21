/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.ContainerWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.Alignment
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.Edges
import dev.icerock.moko.widgets.utils.UIViewWithIdentifier
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applySizeToChild
import dev.icerock.moko.widgets.utils.identifier
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class ContainerViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background<out Fill>?
) : ViewFactory<ContainerWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ContainerWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val viewController: UIViewController = viewFactoryContext

        val root = UIViewWithIdentifier().apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            accessibilityIdentifier = widget.identifier()
        }

        widget.children.forEach { (childWidget, childAlignment) ->
            val childViewBundle = childWidget.buildView(viewController)
            val childView = childViewBundle.view
            childView.translatesAutoresizingMaskIntoConstraints = false

            root.addSubview(childView)

            val edges: Edges<CGFloat> = applySizeToChild(
                rootView = root,
                rootPadding = padding,
                childView = childView,
                childSize = childViewBundle.size,
                childMargins = childViewBundle.margins
            )

            when (childAlignment) {
                Alignment.CENTER -> {
                    childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active =
                        true
                    childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active =
                        true
                }
                Alignment.LEFT -> {
                    childView.leadingAnchor.constraintEqualToAnchor(
                        anchor = root.leadingAnchor,
                        constant = edges.leading
                    ).active = true
                    childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active =
                        true
                }
                Alignment.RIGHT -> {
                    childView.trailingAnchor.constraintEqualToAnchor(
                        anchor = root.trailingAnchor,
                        constant = edges.trailing
                    ).active = true
                    childView.centerYAnchor.constraintEqualToAnchor(root.centerYAnchor).active =
                        true
                }
                Alignment.TOP -> {
                    childView.topAnchor.constraintEqualToAnchor(
                        anchor = root.topAnchor,
                        constant = edges.top
                    ).active = true
                    childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active =
                        true
                }
                Alignment.BOTTOM -> {
                    childView.bottomAnchor.constraintEqualToAnchor(
                        anchor = root.bottomAnchor,
                        constant = edges.bottom
                    ).active = true
                    childView.centerXAnchor.constraintEqualToAnchor(root.centerXAnchor).active =
                        true
                }
            }
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = margins
        )
    }
}
