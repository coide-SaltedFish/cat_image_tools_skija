package element.elements

import draw.Draw
import draw.effect.ShapeShadow
import draw.utils.buildDraw
import element.AbstractElement
import element.measure.ShadowInfo
import element.measure.size.FloatSize
import org.jetbrains.skia.*
import utils.paint
import utils.saveBlock

/**
 * 圆角矩形
 */
class RRectElement(
    var rRect: RRect,
    override var shadowInfo: ShadowInfo? = null,
    var paintBuilder: Paint.() -> Unit = {}
): AbstractElement(), ShapeShadow {
    constructor(width: Float, height: Float, tlRad: Float, trRad: Float, brRad: Float, blRad: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, tlRad, trRad, brRad, blRad), shadowInfo, paintBuilder)
    constructor(width: Float, height: Float, radius: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, radius), shadowInfo, paintBuilder)
    constructor(width: Float, height: Float, xRad: Float, yRad: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {}): this(RRect.makeXYWH(0f, 0f, width, height, xRad, yRad), shadowInfo, paintBuilder)

    private val path: Path get() = path()

    init {
        beforeDrawChain.plus(shapeShadowDraw())

        elementDraw = buildDraw {
            saveBlock({
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(size.width - padding.width, size.height - padding.height))
            }) {
                drawPath(path, paint(paintBuilder))
            }
        }
    }

    override fun autoSize(): FloatSize {
        return FloatSize(rRect.width, rRect.height).add(padding.size())
    }

    override fun shapeShadowDraw(): Draw = buildDraw {
        shadowInfo?.let {
            saveBlock({
                translate(padding.left, padding.top)
                clipPath(path, ClipMode.DIFFERENCE, antiAlias = true)
            }) {
                drawPath(path, paint {
                    imageFilter = it.getDropShadowImageFilterOnly()
                })
            }
        }
    }

    override fun path(): Path {
        return Path().addRRect(rRect)
    }
}