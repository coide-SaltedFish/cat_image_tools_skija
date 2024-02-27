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
                clipRect(Rect.makeWH(size.width, size.height))
            }) {
                drawPath(path, paint(paintBuilder))
            }
        }
    }

    override fun width(): Float = rRect.width + padding.width
    override fun height(): Float = rRect.height + padding.height

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