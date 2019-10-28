package dev.icerock.moko.widgets.style.view

/**
 * Desired widget size defined as specs for width and height.
 *
 * @property width widget's desirable width, could be either one of SizeSpecs or an exact value in dp
 * @property height widget's desirable height, could be either one of SizeSpecs or an exact value in dp
 */
class WidgetSize(
    val width: Int = SizeSpec.WRAP_CONTENT,
    val height: Int = SizeSpec.WRAP_CONTENT
)

/**
 * Size specs.
 *
 * @AS_PARENT - dimension is expected to be same as parent's with respect for margins and parent's paddings
 * @WRAP_CONTENT - dimension is expected to be wrapping child views with respect for paddings
 *
 */
object SizeSpec {
    const val AS_PARENT: Int = -1
    const val WRAP_CONTENT: Int = -2
}