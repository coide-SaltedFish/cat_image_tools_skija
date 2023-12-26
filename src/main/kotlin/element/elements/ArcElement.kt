package element.elements

import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatSize
import org.jetbrains.skia.*
import utils.center
import utils.paint
import utils.saveBlock
import utils.size

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
    var paintBuilder: Paint.() -> Unit = {}
): AbstractElement() {
    companion object{
        /**
         * 构建一个圆形
         */
        fun circular(radius: Float, paintBuilder: Paint.() -> Unit = {}) = ArcElement(Rect.makeWH(radius * 2, radius * 2), 0f, 360f, false, paintBuilder)
    }

    init {
        elementDraw = buildDraw {
            saveBlock({
                clipRect(Rect.makeXYWH(padding.left, padding.top, size.width - padding.width, size.height - padding.height))
                translate(padding.left, padding.top)
            }) {
                drawArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, includeCenter, buildPaint())
            }
        }
    }

    /**
     * 阴影绘制
     */
    fun shadow(shadowInfo: ShadowInfo){
        beforeDrawChain.plus(buildDraw {
            saveBlock({
                val oval = Rect.makeXYWH(padding.left, padding.top, oval.width, oval.height)
                clipPath(Path().arcTo(oval, startAngle, minOf(359f, sweepAngle), includeCenter), ClipMode.DIFFERENCE, antiAlias = true)
                translate(padding.left, padding.top)
            }) {
                drawArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, minOf(359f, sweepAngle), includeCenter, paint {
                    imageFilter = shadowInfo.getDropShadowImageFilterOnly()
                })
            }
        })
    }

    private fun buildPaint() = paint(paintBuilder)

    override fun autoSize(): FloatSize {
        return oval.size().add(padding.size())
    }
}