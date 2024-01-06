package org.sereinfish.catcat.image.skiko.tools.draw.draws

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.utils.paint

class ElementColorDraw(
    val element: Element,
    var color: Int,
    var paintBuilder: Paint.() -> Unit = {}
): Draw {
    override fun draw(canvas: Canvas, context: Context) {
        canvas.drawRect(Rect.makeWH(element.size.width, element.size.height), paint {
            color = this@ElementColorDraw.color
        }.apply(paintBuilder))
    }
}