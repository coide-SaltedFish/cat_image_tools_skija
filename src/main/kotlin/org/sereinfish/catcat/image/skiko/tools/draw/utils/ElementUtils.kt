package org.sereinfish.catcat.image.skiko.tools.draw.utils

import org.jetbrains.skia.Path
import org.sereinfish.catcat.image.skiko.tools.draw.effect.blur
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

object ElementUtils {
    /**
     * 背景模糊
     */
    fun blurBackground(
        element: AbstractElement,
        sigmaX: Float,
        sigmaY: Float,
        clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
        offset: FloatOffset = FloatOffset(), // 偏移
        isFirst: Boolean = false
    ){
        element.apply {
            val draw = buildDraw {
                saveBlock({
                    translate(offset.x, offset.y)
                }) {
                    val s = size.copy()
                    val absPos = Rect.makeXYWH(attributes.offsetAbsolute.x, attributes.offsetAbsolute.y, s.width, s.height)

                    blur(absPos, sigmaX, sigmaY, clipPath?.let {
                        Path().apply {
                            it(element)
                        }
                    }, it as ElementDrawContext)
                }
            }
            if (isFirst) beforeDrawChain.draws.addFirst(draw) else beforeDrawChain.plus(draw)
        }
    }

    fun blurBackground(
        element: AbstractElement,
        radius: Float,
        clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
        offset: FloatOffset = FloatOffset(),
        isFirst: Boolean = false
    ) = blurBackground(element, radius, radius, clipPath, offset, isFirst)

    /**
     * 元素模糊
     */
    fun blur(element: Element, sigmaX: Float, sigmaY: Float, clipPath: Path? = null){
        element.apply {
            val draw = buildDraw {
                val s = size.copy()
                val absPos = Rect.makeXYWH(attributes.offsetAbsolute.x, attributes.offsetAbsolute.y, s.width, s.height)

                blur(absPos, sigmaX, sigmaY, clipPath, it as ElementDrawContext)
            }
            afterDrawChain.plus(draw)
        }
    }
}

fun AbstractElement.blurBackground(
    sigmaX: Float,
    sigmaY: Float,
    clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
    offset: FloatOffset = FloatOffset(),
    isFirst: Boolean = false
) =
    ElementUtils.blurBackground(this, sigmaX, sigmaY, clipPath, offset, isFirst)
fun AbstractElement.blurBackground(
    radius: Float,
    clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
    offset: FloatOffset = FloatOffset(),
    isFirst: Boolean = false
) = ElementUtils.blurBackground(this, radius, clipPath, offset, isFirst)

