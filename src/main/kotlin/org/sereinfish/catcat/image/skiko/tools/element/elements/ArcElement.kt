package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.draw.effect.ShapeShadow
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock
import org.sereinfish.catcat.image.skiko.tools.utils.size

/**
 * 圆弧元素
 */
class ArcElement(
    var oval: Rect,
    var startAngle: Float,
    var sweepAngle: Float,
    var includeCenter: Boolean, // 决定两个端点是相互连接还是各自和圆心连接
    override var shadowInfo: ShadowInfo? = null,
    var paintBuilder: Paint.() -> Unit = {}
): AbstractElement(), ShapeShadow {

    private val path: Path get() = path()
    private val realSweepAngle: Float get() = minOf(359.9f, sweepAngle) // 计算正确的旋转角度

    /**
     * 弧形大小缓存，一次绘制只应该更新一次
     */
    protected var arcRect: Rect by attributes.valueOrElse { arcRect() }

    companion object{
        /**
         * 构建一个圆形
         */
        fun circular(radius: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}) = ArcElement(Rect.makeWH(radius * 2, radius * 2), 0f, 360f, false, shadowInfo, paintBuilder)
    }

    init {
        beforeDrawChain.plus(shapeShadowDraw())

        elementDraw = buildDraw {
            saveBlock({
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(arcRect.width, arcRect.height))
                translate(- arcRect.left, - arcRect.top)

            }) {
                drawPath(path, buildPaint())
            }
        }
    }

    private fun buildPaint() = paint(paintBuilder)

    override fun width(): Float =
        arcRect.size().width + padding.width

    override fun height(): Float =
        arcRect.size().height + padding.height

    /**
     * 对弧形的宽高进行精确计算
     */
    private fun arcRect(): Rect = path.computeTightBounds()

    override fun shapeShadowDraw(): Draw = buildDraw {
        shadowInfo?.let {
            saveBlock({
                translate(padding.left, padding.top)

                clipPath(path, ClipMode.DIFFERENCE, antiAlias = true)

                translate(- arcRect.left, - arcRect.top)
            }) {
                drawPath(path, paint {
                    imageFilter = it.getDropShadowImageFilterOnly()
                })
            }
        }
    }

    override fun path(): Path {
        return Path().apply {
            arcTo(Rect.makeWH(oval.width, oval.height), startAngle, realSweepAngle, includeCenter)
        }
    }
}