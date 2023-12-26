package element.elements

import draw.filter.makeEdgeExtension
import draw.utils.buildDraw
import element.AbstractElement
import element.measure.CropLayout
import element.measure.CropMode
import element.measure.ElementSizeMode
import element.measure.ElementSizeMode.*
import element.measure.offset.FloatOffset
import element.measure.size.FloatSize
import org.jetbrains.skia.*
import utils.paint
import utils.saveBlock
import utils.size

/**
 * 图片元素
 *
 * 支持图片的缩放
 */
class ImageElement(
    var image: Image, // 显示的图片
    override var cropMode: CropMode = CropMode.Fit, // 图片缩放模式
    var samplingMode: SamplingMode = SamplingMode.DEFAULT, // 图片采样模式
    var paintBuilder: Paint.() -> Unit = {}, // 构建 Paint
): AbstractElement(), CropLayout {

    init {
        elementDraw = buildDraw { context ->
            saveBlock({
                clipRect(Rect.makeXYWH(padding.left, padding.top, size.width - padding.width, size.height - padding.height))
                translate(padding.left, padding.top)
            }) {
                val pos = crop(size.minus(padding.size()), image.size())
                drawImageRect(image, pos.first.rect(), pos.second.rect(), samplingMode, buildPaint(), true)
            }
        }
    }

    /**
     * 实时构建 Paint
     */
    private fun buildPaint(): Paint = paint(paintBuilder)

    /**
     * 重写自动大小
     */
    override fun autoSize(): FloatSize {
        return FloatSize(image.width, image.height).add(padding.size())
    }

}