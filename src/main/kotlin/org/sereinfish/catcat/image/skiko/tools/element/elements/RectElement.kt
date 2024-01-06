package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.draw.effect.ShapeShadow
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.ClipMode
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Path
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

class RectElement(
    var width: Float,
    var height: Float,
    var color: Int,
    override var shadowInfo: ShadowInfo? = null,
    var paintBuilder: Paint.(element: RectElement) -> Unit = {},
): AbstractElement(), ShapeShadow {
    private val path: Path get() = path()

    constructor(w: Int, h: Int, color: Int, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.(element: RectElement) -> Unit = {}): this(w.toFloat(), h.toFloat(), color, shadowInfo, paintBuilder)
    constructor(size: FloatSize, color: Int, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.(element: RectElement) -> Unit = {}): this(size.width, size.height, color, shadowInfo, paintBuilder)

    init {
        beforeDrawChain.plus(shapeShadowDraw())

        elementDraw = buildDraw {
            saveBlock({
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(size.width, size.height))
            }) {
                drawPath(path, buildPaint())
            }
        }
    }

    private fun buildPaint() = paint {
        color = this@RectElement.color
        paintBuilder(this@RectElement)
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
        return Path().addRect(Rect.makeWH(size.width, size.height))
    }
}