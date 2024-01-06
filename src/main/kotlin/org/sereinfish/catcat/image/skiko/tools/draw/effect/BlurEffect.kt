package org.sereinfish.catcat.image.skiko.tools.draw.effect

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.draw.filter.makeEdgeExtension
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock
import org.sereinfish.catcat.image.skiko.tools.utils.size

/**
 * 模糊效果
 */
fun Canvas.blur(absPos: Rect, sigmaX: Float, sigmaY: Float, clipPath: Path? = null, context: ElementDrawContext){
    // 模糊
    saveBlock({
        clipPath?.let {
            clipPath(it)
        }
    }) {
        drawImage(context.surface.makeImageSnapshot(), - absPos.left, - absPos.top, paint {
            // 检查是否需要扩展边界
            val (x, y, w, h) = listOf(absPos.left, absPos.top, absPos.width, absPos.height)

            val isWithinBounds = x - sigmaX >= 0 && y - sigmaY >= 0 && x + w + sigmaX <= context.surface.width && y + h + sigmaY <= context.surface.height

            imageFilter = ImageFilter.makeBlur(sigmaX, sigmaY, FilterTileMode.CLAMP, if (isWithinBounds) null else ImageFilter.makeEdgeExtension(absPos.size()))
        })
    }
}

fun Canvas.blur(absPos: Rect, radius: Float, clipPath: Path? = null, context: ElementDrawContext) = blur(absPos, radius, radius, clipPath, context)