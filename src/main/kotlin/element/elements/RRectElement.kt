package element.elements

import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatSize
import org.jetbrains.skia.Paint
import org.jetbrains.skia.RRect
import org.jetbrains.skia.Rect
import utils.paint
import utils.saveBlock

/**
 * 圆角矩形
 */
class RRectElement(
    var rRect: RRect,
    var paintBuilder: Paint.() -> Unit = {}
): AbstractElement() {
    constructor(width: Float, height: Float, tlRad: Float, trRad: Float, brRad: Float, blRad: Float, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, tlRad, trRad, brRad, blRad), paintBuilder)
    constructor(width: Float, height: Float, radius: Float, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, radius), paintBuilder)
    constructor(width: Float, height: Float, xRad: Float, yRad: Float, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, xRad, yRad), paintBuilder)

    init {
        elementDraw = buildDraw {
            saveBlock({
                clipRect(Rect.makeXYWH(padding.left, padding.top, size.width - padding.width, size.height - padding.height))
                translate(padding.left, padding.top)
            }) {
                drawRRect(rRect, paint(paintBuilder))
            }
        }
    }

    /**
     * 阴影绘制
     */
    fun shadow(shadowInfo: ShadowInfo){
        beforeDrawChain.plus(buildDraw {
            saveBlock({
                translate(padding.left, padding.top)
            }) {
                drawRRect(rRect, paint {
                    imageFilter = shadowInfo.getDropShadowImageFilterOnly()
                })
            }
        })
    }

    override fun autoSize(): FloatSize {
        return FloatSize(rRect.width, rRect.height).add(padding.size())
    }
}