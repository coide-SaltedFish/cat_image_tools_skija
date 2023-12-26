package draw.effect

import draw.utils.buildDraw
import element.AbstractElement
import element.Element
import element.measure.ShadowInfo
import org.jetbrains.skia.*
import utils.paint
import utils.saveBlock

/**
 * 设置元素的形状限制
 */
fun AbstractElement.shape(path: Path, mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = false){
    beforeDrawChain.plus(buildDraw {
        save()
        clipPath(path, mode, antiAlias)
    })

    afterDrawChain.plus(buildDraw {
        restore()
    })
}

/**
 * 设置元素圆角形状
 */
fun AbstractElement.rRectShape(
    rRectBuilder: AbstractElement.() -> RRect,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = false
){
    beforeDrawChain.draws.addFirst(buildDraw {
        save()
        clipRRect(rRectBuilder(), mode, antiAlias)
    })

    afterDrawChain.plus(buildDraw {
        restore()

        if (stroke){
            drawRRect(rRectBuilder(), paint {
                this.mode = PaintMode.STROKE
                this.color = strokeColor
                this.strokeWidth = strokeWidth.toFloat()
            })
        }
        shadowInfo?.let {
            saveBlock({
                clipRRect(rRectBuilder(), ClipMode.DIFFERENCE, antiAlias)
            }) {
                drawRRect(rRectBuilder(), paint {
                    imageFilter = it.getDropShadowImageFilterOnly()
                })
            }
        }
    })
}

fun AbstractElement.rRectShape(
    tlRad: Number = 0f, trRad: Number = 0f, brRad: Number = 0f, blRad: Number = 0f,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: Boolean = false,
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = false
){
    rRectShape({
        if (padding){
            val size = size.copy().minus(this.padding.size())
            RRect.makeXYWH(this.padding.left, this.padding.top, size.width, size.height, tlRad.toFloat(), trRad.toFloat(), brRad.toFloat(), blRad.toFloat())
        } else RRect.makeXYWH(0f, 0f, size.width, size.height, tlRad.toFloat(), trRad.toFloat(), brRad.toFloat(), blRad.toFloat())
    }, stroke, strokeColor, strokeWidth, shadowInfo, mode, antiAlias)
}

fun AbstractElement.rRectShape(
    xRad: Number, yRad: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: Boolean = false,
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = false
){
    rRectShape({
        if (padding) {
            val size = size.copy().minus(this.padding.size())
            RRect.makeXYWH(this.padding.left, this.padding.top, size.width, size.height, xRad.toFloat(), yRad.toFloat())
        }else RRect.makeXYWH(0f, 0f, size.width, size.height, xRad.toFloat(), yRad.toFloat())
    }, stroke, strokeColor, strokeWidth, shadowInfo, mode, antiAlias)
}

fun AbstractElement.rRectShape(
    radius: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: Boolean = false,
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = false
){
    rRectShape(radius, radius, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}