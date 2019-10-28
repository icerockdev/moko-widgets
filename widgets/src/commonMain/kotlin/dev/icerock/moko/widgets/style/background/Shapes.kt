package com.icerockdev.mpp.widgets.style.background

/**
 * Defines a shape with specified corners radii.
 */
class Shape(val type: ShapeType = ShapeType.RECTANGLE, val corners: Corners = Corners())

/**
 * Possible shape types
 */
enum class ShapeType {
    OVAL, RECTANGLE
}

class Corners(
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