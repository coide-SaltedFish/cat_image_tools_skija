package element.elements

import draw.Draw
import draw.effect.Shape
import draw.effect.ShapeShadow
import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatRectSize
import element.measure.size.FloatSize
import org.jetbrains.skia.*
import utils.center
import utils.paint
import utils.saveBlock
import utils.size
import kotlin.math.sin

/**
 * 圆弧元素
 *
 * TODO 完成弧形的高宽精确计算
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

    companion object{
        /**
         * 构建一个圆形
         */
        fun circular(radius: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}) = ArcElement(Rect.makeWH(radius * 2, radius * 2), 0f, 360f, false, shadowInfo, paintBuilder)
    }

    init {
        beforeDrawChain.plus(buildDraw {

        })

        beforeDrawChain.plus(shapeShadowDraw())

        elementDraw = buildDraw {
            saveBlock({
                val bound = arcRect()
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(bound.width, bound.height))
                translate(- bound.left, - bound.top)

            }) {
                drawPath(path, buildPaint())
            }
        }
    }

    private fun buildPaint() = paint(paintBuilder)

    override fun autoSize(): FloatSize {
        return arcRect().size().add(padding.size())
    }

    /**
     * 对弧形的宽高进行精确计算
     */
    private fun arcRect(): Rect = path.computeTightBounds()

    override fun shapeShadowDraw(): Draw = buildDraw {
        shadowInfo?.let {
            saveBlock({
                val bound = arcRect()
                translate(padding.left, padding.top)

                clipPath(path, ClipMode.DIFFERENCE, antiAlias = true)

                translate(- bound.left, - bound.top)
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