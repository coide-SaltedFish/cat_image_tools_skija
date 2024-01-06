package org.sereinfish.catcat.image.skiko.tools.draw.draws

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.utils.paint

/**
 * 元素矩形描边效果
 */
class ElementStrokeDraw(
    val element: Element,
    val strokeWidth: Float = 1f,
    val color: Int = Color.BLACK
): Draw {
    override fun draw(canvas: Canvas, context: Context) {
        canvas.drawRect(Rect.Companion.makeXYWH(strokeWidth, strokeWidth, element.size.width - strokeWidth, element.size.height - strokeWidth), paint {
            color = this@ElementStrokeDraw.color
            strokeWidth = this@ElementStrokeDraw.strokeWidth

            mode = PaintMode.STROKE
        })
    }
}