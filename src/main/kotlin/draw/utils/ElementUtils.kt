package draw.utils

import draw.effect.blur
import element.AbstractElement
import element.Element
import element.context.ElementDrawContext
import org.jetbrains.skia.Rect

/**
 * 背景模糊
 */
fun AbstractElement.blurBackground(sigmaX: Float, sigmaY: Float, isFirst: Boolean = false){
    val draw = buildDraw {
        val s = size.copy()
        val absPos = Rect.makeXYWH(attributes.offsetAbsolute.x, attributes.offsetAbsolute.y, s.width, s.height)

        blur(absPos, sigmaX, sigmaY, it as ElementDrawContext)
    }
    if (isFirst) beforeDrawChain.draws.addFirst(draw) else beforeDrawChain.plus(draw)

}

fun AbstractElement.blurBackground(radius: Float) = blurBackground(radius, radius)

