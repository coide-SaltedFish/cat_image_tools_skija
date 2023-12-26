package element.elements

import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatSize
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import utils.paint
import utils.saveBlock

class RectElement(
    var width: Float,
    var height: Float,
    var color: Int,
    var paintBuilder: Paint.() -> Unit = {}
): AbstractElement() {
    constructor(w: Int, h: Int, color: Int, paintBuilder: Paint.() -> Unit = {}): this(w.toFloat(), h.toFloat(), color, paintBuilder)
    constructor(size: FloatSize, color: Int, paintBuilder: Paint.() -> Unit = {}): this(size.width, size.height, color, paintBuilder)

    init {
        elementDraw = buildDraw {
            saveBlock({
                clipRect(Rect.makeXYWH(padding.left, padding.top, size.width - padding.width, size.height - padding.height))
                translate(padding.left, padding.top)
            }) {
                drawRect(Rect.makeWH(width, height), buildPaint())
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
                drawRect(Rect.makeWH(width, height), paint {
                    imageFilter = shadowInfo.getDropShadowImageFilterOnly()
                })
            }
        })
    }

    private fun buildPaint() = paint {
        color = this@RectElement.color
        paintBuilder()
    }

    override fun autoSize(): FloatSize {
        return FloatSize(width, height).add(padding.size())
    }
}