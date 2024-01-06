package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.jetbrains.skia.ClipMode
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Path
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock
import org.sereinfish.catcat.image.skiko.tools.utils.size

/**
 * 形状元素
 */
class ShapeElement(
    var path: Path.(element: ShapeElement) -> Unit,
    var antiAlias: Boolean = true,
    var shadowInfo: ShadowInfo? = null,
    var paintBuilder: Paint.() -> Unit
): AbstractElement() {

    private val pathBuild: Path get() = Path().apply {
        path(this@ShapeElement)
    }

    init {
        elementDraw = buildDraw {

            shadowInfo?.let {
                saveBlock({
                    translate(padding.left, padding.top)
                    clipPath(pathBuild, ClipMode.DIFFERENCE, antiAlias = antiAlias)
                }) {
                    drawPath(pathBuild, paint {
                        imageFilter = it.getDropShadowImageFilterOnly()
                    })
                }
            }

            saveBlock({
                val bound = pathBuild.computeTightBounds()
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(bound.width, bound.height))
                translate(- bound.left, - bound.top)
            }) {

                drawPath(pathBuild, paint(paintBuilder))
            }
        }
    }

    override fun autoSize(): FloatSize {
        return pathBuild.computeTightBounds().size().add(padding.size())
    }
}