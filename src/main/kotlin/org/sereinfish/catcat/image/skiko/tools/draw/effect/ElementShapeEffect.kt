package org.sereinfish.catcat.image.skiko.tools.draw.effect

import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.A
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock



/**
 * 设置元素的形状限制
 */
fun AbstractElement.shape(
    path: AbstractElement.() -> Path,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
){
    beforeDrawChain.plus(buildDraw {
        save()
        clipPath(Path().apply {
            this.addPath(path(), padding.left, padding.top)
        }, mode, antiAlias)
    })

    afterDrawChain.plus(buildDraw {
        restore()
        saveBlock({
            translate(padding.left, padding.top)
        }) {
            val p = path()

            if (stroke){
                drawPath(p, paint {
                    this.mode = PaintMode.STROKE
                    this.color = strokeColor
                    this.strokeWidth = strokeWidth.toFloat()
                })
            }
            shadowInfo?.let {
                saveBlock({
                    clipPath(p, ClipMode.DIFFERENCE, antiAlias)
                }) {
                    drawPath(p, paint {
                        imageFilter = it.getDropShadowImageFilterOnly()
                    })
                }
            }
        }
    })
}

/**
 * 绘制圆角形状
 */
fun AbstractElement.rRectShape(
    tlRad: Number, trRad: Number, brRad: Number, blRad: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = shape({
    val s = size.copy().minus(padding.size())
    Path().addRRect(RRect.makeXYWH(0f, 0f, s.width, s.height, tlRad.toFloat(), trRad.toFloat(), brRad.toFloat(), blRad.toFloat()))
}, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)

fun AbstractElement.rRectShape(
    xRad: Number, yRad: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = shape({
    val s = size.copy().minus(padding.size())
    Path().addRRect(RRect.makeXYWH(0f, 0f, s.width, s.height, xRad.toFloat(), yRad.toFloat()))
}, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)


fun AbstractElement.rRectShape(
    radius: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = rRectShape(radius, radius, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)

/**
 * 绘制圆形
 */
fun AbstractElement.arcShape(
    startAngle: Float, sweepAngle: Float, includeCenter: Boolean,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = shape({
    val s = size.copy().minus(padding.size())
    Path().arcTo(Rect.makeWH(s.width, s.height), startAngle, minOf(359.9f, sweepAngle), includeCenter)
}, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)

fun AbstractElement.circularShape(
    includeCenter: Boolean = true,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = shape({
    val s = size.copy().minus(padding.size())

    val d = minOf(s.width, s.height)
    val x = (s.width - d) / 2
    val y = (s.height - d) / 2

    Path().arcTo(Rect.makeXYWH(x, y, d, d), 0f, 359.9f, includeCenter)
}, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)