/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.factory

import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize
import dev.icerock.moko.widgets.core.widget.VerticalPageWidget
import platform.UIKit.UIView
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual open class VerticalPageViewFactory : ViewFactory<VerticalPageWidget<out WidgetSize>> {
    @Suppress("ComplexMethod", "LongMethod")
    override fun <WS : WidgetSize> build(
        widget: VerticalPageWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val root = UIView().apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        val headerBundle = widget.header?.let { header ->
            val childBundle = header.buildView(viewFactoryContext)
            childBundle.view.translatesAutoresizingMaskIntoConstraints = false
            root.addSubview(childBundle.view)
            childBundle
        }

        val bodyBundle = widget.body.buildView(viewFactoryContext)
        root.addSubview(bodyBundle.view)

        val footerBundle = widget.footer?.let { footer ->
            val childBundle = footer.buildView(viewFactoryContext)
            childBundle.view.translatesAutoresizingMaskIntoConstraints = false
            root.addSubview(childBundle.view)
            childBundle
        }

        fun applyHorizontalConstraints(bundle: ViewBundle<out WidgetSize>) {
            bundle.view.leadingAnchor.constraintEqualToAnchor(
                anchor = root.leadingAnchor,
                constant = bundle.margins?.start?.toDouble() ?: 0.0
            ).active = true

            bundle.view.trailingAnchor.constraintEqualToAnchor(
                anchor = root.trailingAnchor,
                constant = bundle.margins?.end?.toDouble() ?: 0.0
            ).active = true
        }

        val safeArea = root.safeAreaLayoutGuide

        if (headerBundle != null) {
            applyHorizontalConstraints(headerBundle)

            headerBundle.view.topAnchor.constraintEqualToAnchor(
                anchor = safeArea.topAnchor,
                constant = headerBundle.margins?.top?.toDouble() ?: 0.0
            ).active = true

            bodyBundle.view.topAnchor.constraintEqualToAnchor(
                anchor = headerBundle.view.bottomAnchor,
                constant = (headerBundle.margins?.bottom?.toDouble() ?: 0.0) +
                        (bodyBundle.margins?.top?.toDouble() ?: 0.0)
            ).active = true
        } else {
            bodyBundle.view.topAnchor.constraintEqualToAnchor(
                anchor = safeArea.topAnchor,
                constant = bodyBundle.margins?.top?.toDouble() ?: 0.0
            ).active = true
        }

        applyHorizontalConstraints(bodyBundle)

        if (footerBundle != null) {
            applyHorizontalConstraints(footerBundle)

            footerBundle.view.bottomAnchor.constraintEqualToAnchor(
                anchor = safeArea.bottomAnchor,
                constant = footerBundle.margins?.bottom?.toDouble() ?: 0.0
            ).active = true

            bodyBundle.view.bottomAnchor.constraintEqualToAnchor(
                anchor = footerBundle.view.topAnchor,
                constant = (footerBundle.margins?.top?.toDouble() ?: 0.0) +
                        (bodyBundle.margins?.bottom?.toDouble() ?: 0.0)
            ).active = true
        } else {
            bodyBundle.view.bottomAnchor.constraintEqualToAnchor(
                anchor = safeArea.bottomAnchor,
                constant = bodyBundle.margins?.bottom?.toDouble() ?: 0.0
            ).active = true
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = null
        )
    }
}
