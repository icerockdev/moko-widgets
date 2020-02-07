package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import platform.UIKit.UIViewController
import platform.UIKit.UIView

var floatPanelShow: ((vc: UIViewController, body: UIView) -> Unit)? = null

actual fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>
) {
    floatPanelShow?.let { it(this.viewController, content.buildView(viewFactoryContext = this.viewController).view) }
}
