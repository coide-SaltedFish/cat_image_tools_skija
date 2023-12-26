package element.elements

import draw.Draw
import draw.effect.ShapeShadow
import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatSize
import org.jetbrains.skia.ClipMode
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Path
import org.jetbrains.skia.Rect
import utils.paint
import utils.saveBlock

class RectElement(
    var width: Float,
    var height: Float,
    var color: Int,
    override var shadowInfo: ShadowInfo? = null,
    var paintBuilder: Paint.() -> Unit = {},
): AbstractElement(), ShapeShadow {
    private val path: Path get() = path()

    constructor(w: Int, h: Int, color: Int, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}): this(w.toFloat(), h.toFloat(), color, shadowInfo, paintBuilder)
    constructor(size: FloatSize, color: Int, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}): this(size.width, size.height, color, shadowInfo, paintBuilder)

    init {
        beforeDrawChain.plus(shapeShadowDraw())

        elementDraw = buildDraw {
            saveBlock({
                clipRect(Rect.makeXYWH(padding.left, padding.top, size.width - padding.width, size.height - padding.height))
                translate(padding.left, padding.top)
            }) {
                drawPath(path, buildPaint())
            }
        }
    }

    private fun buildPaint() = paint {
        color = this@RectElement.color
        paintBuilder()
    }

    override fun autoSize(): FloatSize {
        return FloatSize(width, height).add(padding.size())
    }

    override fun shapeShadowDraw(): Draw = buildDraw {
        shadowInfo?.let {
            saveBlock({
                translate(padding.left, padding.top)
                clipPath(path, ClipMode.DIFFERENCE)
            }) {
                drawPath(path, paint {
                    imageFilter = it.getDropShadowImageFilterOnly()
                })
            }
        }
    }

    override fun path(): Path {
        return Path().addRect(Rect.makeWH(width, height))
    }
}