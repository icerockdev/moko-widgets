package dev.icerock.moko.widgets.screen

import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
//import platform.FloatingPanel
import platform.UIKit.UIViewController
import platform.UIKit.UIView

var floatPanelShow: ((vc: UIViewController, body: UIView) -> Unit)? = null

actual fun Screen<*>.showBottomSheet(
    content: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.WrapContent>>
) {
    // floatPanelShow(this.viewController, content.buildView().view)
    floatPanelShow?.let { it(this.viewController, content.buildView(viewFactoryContext = this.viewController).view) }
}

// а вот это вызвать на нативе. На каком контроллере отобразить эту вьюху
//BottomSheetKt.floatPanelShow = { }
