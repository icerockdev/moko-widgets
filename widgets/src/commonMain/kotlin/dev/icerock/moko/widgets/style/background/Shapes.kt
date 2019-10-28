package dev.icerock.moko.widgets.style.background

/**
 * Defines a shape with specified corners radii.
 */
data class Shape(
    val type: ShapeType = ShapeType.RECTANGLE,
    val corners: Corners = Corners()
)

/**
 * Possible shape types
 */
enum class ShapeType {
    OVAL, RECTANGLE
}

data class Corners(
    val topLeft: Float = 0.0F,
    val topRight: Float = 0.0F,
    val bottomRight: Float = 0.0F,
    val bottomLeft: Float = 0.0F
) {
    constructor(radii: Float) : this(
        topLeft = radii,
        topRight = radii,
        bottomRight = radii,
        bottomLeft = radii
    )
}