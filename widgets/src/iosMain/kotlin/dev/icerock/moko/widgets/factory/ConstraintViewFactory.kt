/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import dev.icerock.moko.widgets.Constraint
import dev.icerock.moko.widgets.ConstraintItem
import dev.icerock.moko.widgets.ConstraintWidget
import dev.icerock.moko.widgets.ConstraintsApi
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.background.Background
import dev.icerock.moko.widgets.style.background.Fill
import dev.icerock.moko.widgets.style.view.MarginValues
import dev.icerock.moko.widgets.style.view.PaddingValues
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.applyBackgroundIfNeeded
import dev.icerock.moko.widgets.utils.applySizeToChild
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutAnchor
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UILayoutGuide
import platform.UIKit.UIView
import platform.UIKit.addLayoutGuide
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.centerXAnchor
import platform.UIKit.centerYAnchor
import platform.UIKit.leftAnchor
import platform.UIKit.rightAnchor
import platform.UIKit.safeAreaLayoutGuide
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

actual class ConstraintViewFactory actual constructor(
    private val background: Background<out Fill>?,
    private val padding: PaddingValues?,
    private val margins: MarginValues?
) : ViewFactory<ConstraintWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: ConstraintWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val container = UIView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false

            applyBackgroundIfNeeded(background)
        }

        val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>> =
            widget.children.associateWith { childWidget ->
                val viewBundle = childWidget.buildView(viewFactoryContext)

                val childView = viewBundle.view

                container.addSubview(childView)

                viewBundle
            }

        val constraintsHandler = AutoLayoutConstraintsApi(
            container = container,
            widgetViewBundle = widgetViewBundle,
            padding = padding
        )

        widget.constraints.invoke(constraintsHandler)

        widgetViewBundle.forEach { (childWidget, viewBundle) ->
            val childView = viewBundle.view
            val childMargins = viewBundle.margins
            val extraMargins = constraintsHandler.widgetAdditionalMargins[childWidget]
            val margins = when {
                childMargins != null && extraMargins != null -> childMargins + extraMargins
                childMargins != null -> childMargins
                extraMargins != null -> extraMargins
                else -> null
            }

            applySizeToChild(
                rootView = container,
                rootPadding = padding,
                childView = childView,
                childSize = viewBundle.size,
                childMargins = margins
            )
        }

        return ViewBundle(
            view = container,
            size = size,
            margins = margins
        )
    }
}

@Suppress("TooManyFunctions")
class AutoLayoutConstraintsApi(
    private val container: UIView,
    private val widgetViewBundle: Map<out Widget<out WidgetSize>, ViewBundle<out WidgetSize>>,
    private val padding: PaddingValues?
) : ConstraintsApi {
    class AnchorSet private constructor(
        val topAnchor: NSLayoutAnchor,
        val leftAnchor: NSLayoutAnchor,
        val rightAnchor: NSLayoutAnchor,
        val bottomAnchor: NSLayoutAnchor,
        val centerXAnchor: NSLayoutAnchor,
        val centerYAnchor: NSLayoutAnchor
    ) {
        constructor(view: UIView) : this(
            topAnchor = view.topAnchor,
            leftAnchor = view.leftAnchor,
            rightAnchor = view.rightAnchor,
            bottomAnchor = view.bottomAnchor,
            centerXAnchor = view.centerXAnchor,
            centerYAnchor = view.centerYAnchor
        )

        constructor(guide: UILayoutGuide) : this(
            topAnchor = guide.topAnchor,
            leftAnchor = guide.leftAnchor,
            rightAnchor = guide.rightAnchor,
            bottomAnchor = guide.bottomAnchor,
            centerXAnchor = guide.centerXAnchor,
            centerYAnchor = guide.centerYAnchor
        )
    }

    private val _widgetAdditionalMargins = mutableMapOf<Widget<out WidgetSize>, MarginValues>()
    val widgetAdditionalMargins: Map<out Widget<out WidgetSize>, MarginValues> = _widgetAdditionalMargins

    private fun applyAdditionalMargins(
        widget: Widget<out WidgetSize>,
        offset: Float,
        transform: MarginValues.(Float) -> MarginValues
    ) {
        val currentMargins = _widgetAdditionalMargins[widget] ?: MarginValues()
        val newMargins = currentMargins.transform(offset)
        _widgetAdditionalMargins[widget] = newMargins
    }

    private fun ConstraintItem.view(): UIView {
        return when (this) {
            ConstraintItem.Root -> container
            // api of widget will not accept case of null
            is ConstraintItem.Child -> widgetViewBundle[this.widget]!!.view
            is ConstraintItem.SafeArea -> container
        }
    }

    private fun ConstraintItem.anchorSet(): AnchorSet {
        return when (this) {
            ConstraintItem.Root -> AnchorSet(container)
            is ConstraintItem.Child -> AnchorSet(this.view())
            is ConstraintItem.SafeArea -> AnchorSet(container.safeAreaLayoutGuide)
        }
    }

    private fun ConstraintItem.Child.viewBundle(): ViewBundle<out WidgetSize> {
        return widgetViewBundle[this.widget]!! // api of widget will not accept case of null
    }

    private fun ConstraintItem.Child.calcConstant(
        to: ConstraintItem,
        marginGetter: MarginValues.() -> Float,
        paddingGetter: PaddingValues.() -> Float
    ): CGFloat {
        var const: CGFloat = viewBundle().margins?.let(marginGetter)?.toDouble() ?: 0.0
        if (to is ConstraintItem.Root) const += padding?.let(paddingGetter)?.toDouble() ?: 0.0
        return const
    }

    @Suppress("LongParameterList")
    private fun constraint(
        firstItem: ConstraintItem.Child,
        secondItem: ConstraintItem,
        firstMargin: MarginValues.() -> Float = { 0.0f },
        secondPadding: PaddingValues.() -> Float = { 0.0f },
        applyExtraMargins: (MarginValues.(Float) -> MarginValues)? = null,
        block: AnchorSet.(AnchorSet) -> NSLayoutConstraint
    ): Constraint {
        val firstView = firstItem.anchorSet()
        val secondView = secondItem.anchorSet()

        val const = firstItem.calcConstant(
            secondItem,
            marginGetter = firstMargin,
            paddingGetter = secondPadding
        )

        return block(firstView, secondView).apply {
            constant = const
            active = true
        }.let {
            object : Constraint {
                override fun offset(points: Int) {
                    it.constant = const + points
                    if (applyExtraMargins != null) {
                        applyAdditionalMargins(firstItem.widget, points.toFloat(), applyExtraMargins)
                    }
                }
            }
        }
    }

    override fun ConstraintItem.Child.leftToRight(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.start },
            secondPadding = { this.end },
            applyExtraMargins = { copy(start = it) },
            block = { leftAnchor.constraintEqualToAnchor(it.rightAnchor) }
        )
    }

    override fun ConstraintItem.Child.leftToLeft(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.start },
            secondPadding = { this.start },
            applyExtraMargins = { copy(start = it) },
            block = { leftAnchor.constraintEqualToAnchor(it.leftAnchor) }
        )
    }

    override fun ConstraintItem.Child.rightToRight(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.end },
            secondPadding = { this.end },
            applyExtraMargins = { copy(end = it) },
            block = { it.rightAnchor.constraintEqualToAnchor(rightAnchor) }
        )
    }

    override fun ConstraintItem.Child.rightToLeft(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.end },
            secondPadding = { this.start },
            applyExtraMargins = { copy(end = it) },
            block = { it.leftAnchor.constraintEqualToAnchor(rightAnchor) }
        )
    }

    override fun ConstraintItem.Child.topToTop(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.top },
            secondPadding = { this.top },
            applyExtraMargins = { copy(top = it) },
            block = { topAnchor.constraintEqualToAnchor(it.topAnchor) }
        )
    }

    override fun ConstraintItem.Child.topToBottom(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.top },
            secondPadding = { this.bottom },
            applyExtraMargins = { copy(top = it) },
            block = { topAnchor.constraintEqualToAnchor(it.bottomAnchor) }
        )
    }

    override fun ConstraintItem.Child.bottomToBottom(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.bottom },
            secondPadding = { this.bottom },
            applyExtraMargins = { copy(bottom = it) },
            block = { it.bottomAnchor.constraintEqualToAnchor(bottomAnchor) }
        )
    }

    override fun ConstraintItem.Child.bottomToTop(to: ConstraintItem): Constraint {
        return constraint(
            firstItem = this,
            secondItem = to,
            firstMargin = { this.bottom },
            secondPadding = { this.top },
            applyExtraMargins = { copy(bottom = it) },
            block = { it.topAnchor.constraintEqualToAnchor(bottomAnchor) }
        )
    }

    override fun ConstraintItem.Child.centerYToCenterY(to: ConstraintItem) {
        constraint(this, to) { centerYAnchor.constraintEqualToAnchor(it.centerYAnchor) }
    }

    override fun ConstraintItem.Child.centerXToCenterX(to: ConstraintItem) {
        constraint(this, to) { centerXAnchor.constraintEqualToAnchor(it.centerXAnchor) }
    }

    override fun ConstraintItem.Child.verticalCenterBetween(
        top: ConstraintItem.VerticalAnchor,
        bottom: ConstraintItem.VerticalAnchor
    ) {
        val layoutGuide = UILayoutGuide()
        container.addLayoutGuide(layoutGuide)

        val topAnchorSet = top.item.anchorSet()
        val bottomAnchorSet = bottom.item.anchorSet()

        layoutGuide.topAnchor.constraintEqualToAnchor(top.edge.toAnchor(topAnchorSet)).active = true
        layoutGuide.bottomAnchor.constraintEqualToAnchor(bottom.edge.toAnchor(bottomAnchorSet)).active = true

        val childAnchorSet = this.anchorSet()
        childAnchorSet.centerYAnchor.constraintEqualToAnchor(layoutGuide.centerYAnchor).active = true
    }

    private fun ConstraintItem.VerticalAnchor.Edge.toAnchor(anchorSet: AnchorSet): NSLayoutAnchor {
        return when (this) {
            ConstraintItem.VerticalAnchor.Edge.TOP -> anchorSet.topAnchor
            ConstraintItem.VerticalAnchor.Edge.BOTTOM -> anchorSet.bottomAnchor
        }
    }

    private fun ConstraintItem.HorizontalAnchor.Edge.toAnchor(anchorSet: AnchorSet): NSLayoutAnchor {
        return when (this) {
            ConstraintItem.HorizontalAnchor.Edge.LEFT -> anchorSet.leftAnchor
            ConstraintItem.HorizontalAnchor.Edge.RIGHT -> anchorSet.rightAnchor
        }
    }

    override fun ConstraintItem.Child.horizontalCenterBetween(
        left: ConstraintItem.HorizontalAnchor,
        right: ConstraintItem.HorizontalAnchor
    ) {
        val layoutGuide = UILayoutGuide()
        container.addLayoutGuide(layoutGuide)

        val firstAnchorSet = left.item.anchorSet()
        val secondAnchorSet = right.item.anchorSet()

        layoutGuide.leftAnchor.constraintEqualToAnchor(left.edge.toAnchor(firstAnchorSet)).active = true
        layoutGuide.rightAnchor.constraintEqualToAnchor(right.edge.toAnchor(secondAnchorSet)).active = true

        val childAnchorSet = this.anchorSet()
        childAnchorSet.centerXAnchor.constraintEqualToAnchor(layoutGuide.centerXAnchor).active = true
    }

    override fun ConstraintItem.VerticalAnchor.pin(to: ConstraintItem.VerticalAnchor): Constraint {
        // TODO padding margin
        val firstAnchorSet = this.item.anchorSet()
        val secondAnchorSet = to.item.anchorSet()

        val firstAnchor = this.edge.toAnchor(firstAnchorSet)
        val secondAnchor = to.edge.toAnchor(secondAnchorSet)

        return firstAnchor.constraintEqualToAnchor(secondAnchor).let {
            it.active = true
            object : Constraint {
                override fun offset(points: Int) {
                    // TODO padding margin
                    it.constant = points.toDouble()
                }
            }
        }
    }

    override fun ConstraintItem.HorizontalAnchor.pin(to: ConstraintItem.HorizontalAnchor): Constraint {
        // TODO padding margin
        val firstAnchorSet = this.item.anchorSet()
        val secondAnchorSet = to.item.anchorSet()

        val firstAnchor = this.edge.toAnchor(firstAnchorSet)
        val secondAnchor = to.edge.toAnchor(secondAnchorSet)

        return firstAnchor.constraintEqualToAnchor(secondAnchor).let {
            it.active = true
            object : Constraint {
                override fun offset(points: Int) {
                    // TODO padding margin
                    it.constant = points.toDouble()
                }
            }
        }
    }
}
