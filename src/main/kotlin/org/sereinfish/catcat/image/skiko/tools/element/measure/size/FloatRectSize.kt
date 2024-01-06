package org.sereinfish.catcat.image.skiko.tools.element.measure.size

import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.jetbrains.skia.Rect

/**
 * 定义一个矩形大小
 */
data class FloatRectSize(
    var left: Float = 0f,
    var right: Float = 0f,
    var top: Float = 0f,
    var bottom: Float = 0f
){
    constructor(value: Number): this(value.toFloat(), value.toFloat(), value.toFloat(), value.toFloat())

    companion object{
        fun makeXYWH(x: Number, y: Number, w: Number, h: Number) = FloatRectSize(x.toFloat(), x.toFloat() + w.toFloat(), y.toFloat(), y.toFloat() + h.toFloat())
    }

    val width get() = left + right
    val height get() = top + bottom

    fun rect() = Rect.makeLTRB(left, top, right, bottom)

    fun size() = FloatSize(width, height)

    fun offset() = FloatOffset(left, top)

    override fun toString(): String {
        return "FloatRectSize(left=$left, right=$right, top=$top, bottom=$bottom)"
    }
}