package draw.effect

import draw.filter.makeEdgeExtension
import element.context.ElementDrawContext
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.Rect
import utils.paint
import utils.size

/**
 * 模糊效果
 */
fun Canvas.blur(absPos: Rect, sigmaX: Float, sigmaY: Float, context: ElementDrawContext){
    // 模糊

    drawImage(context.surface.makeImageSnapshot(), - absPos.left, - absPos.top, paint {
        // 检查是否需要扩展边界
        val (x, y, w, h) = listOf(absPos.left, absPos.top, absPos.width, absPos.height)

        val isWithinBounds = x - sigmaX >= 0 && y - sigmaY >= 0 && x + w + sigmaX <= context.surface.width && y + h + sigmaY <= context.surface.height

        imageFilter = ImageFilter.makeBlur(sigmaX, sigmaY, FilterTileMode.CLAMP, if (isWithinBounds) null else ImageFilter.makeEdgeExtension(absPos.size()))
    })
}

fun Canvas.blur(absPos: Rect, radius: Float, context: ElementDrawContext) = blur(absPos, radius, radius, context)