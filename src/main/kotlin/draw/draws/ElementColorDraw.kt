package draw.draws

import context.Context
import draw.Draw
import element.Element
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import utils.paint

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