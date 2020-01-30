/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.CardWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.view.*
import dev.icerock.moko.widgets.utils.*
import dev.icerock.moko.widgets.utils.identifier
import kotlinx.cinterop.readValue
import platform.CoreGraphics.*
import platform.UIKit.*

actual class CardViewFactory actual constructor(
    private val padding: PaddingValues?,
    private val margins: MarginValues?,
    private val background: Background?,
    private val cornerRadius: CornerRadiusValue?
) : ViewFactory<CardWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: CardWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {

        val viewController: UIViewController = viewFactoryContext

        val root = UIViewWithIdentifier().apply {
            translatesAutoresizingMaskIntoConstraints = false
            applyBackgroundIfNeeded(background)

            layer.cornerRadius = cornerRadius?.radius?.toDouble() ?: 0.0
            clipsToBounds = true
            backgroundColor = UIColor.whiteColor

            accessibilityIdentifier = widget.identifier()
        }

        val childViewBundle = widget.child.buildView(viewController)
        val childView = childViewBundle.view
        childView.translatesAutoresizingMaskIntoConstraints = false
        root.addSubview(childView)

        val childSize = childViewBundle.size
        val childMargins = childViewBundle.margins

        val edges: Edges<CGFloat> = applySizeToChild(
            rootView = root,
            rootPadding = padding,
            childView = childView,
            childSize = childSize,
            childMargins = childMargins
        )

        childView.leadingAnchor.constraintEqualToAnchor(
            anchor = root.leadingAnchor,
            constant = edges.leading
        ).active = true

        root.trailingAnchor.constraintEqualToAnchor(
            anchor = childView.trailingAnchor,
            constant = edges.trailing
        ).active = true

        childView.topAnchor.constraintEqualToAnchor(
            anchor = root.topAnchor,
            constant = edges.top
        ).active = true

        root.bottomAnchor.constraintEqualToAnchor(
            anchor = childView.bottomAnchor,
            constant = edges.bottom
        ).active = true

        val shadowContainerView = UIView(frame = root.frame).apply {
            translatesAutoresizingMaskIntoConstraints = false
            layer.cornerRadius = cornerRadius?.radius?.toDouble() ?: 0.0

            layer.shadowColor = UIColor.grayColor.CGColor
            layer.shadowOpacity = 0.7f
            layer.shadowOffset = CGSizeMake(0.0, 4.0)
            layer.shadowRadius = 4.0
        }

        shadowContainerView.addSubview(root)
        root.leadingAnchor.constraintEqualToAnchor(shadowContainerView.leadingAnchor).active = true
        shadowContainerView.trailingAnchor.constraintEqualToAnchor(root.trailingAnchor).active = true
        root.topAnchor.constraintEqualToAnchor(shadowContainerView.topAnchor).active = true
        shadowContainerView.bottomAnchor.constraintEqualToAnchor(root.bottomAnchor).active = true

        return ViewBundle(
            view = shadowContainerView,
            size = size,
            margins = margins
        )
    }

}
