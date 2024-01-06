package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock
import org.sereinfish.catcat.image.skiko.tools.utils.size

/**
 * 图片元素
 *
 * 支持图片的缩放
 */
class ImageElement(
    var image: Image, // 显示的图片
    var imageShadowInfo: ShadowInfo? = null,
    var offset: FloatOffset = FloatOffset(), // 图片偏移量
    override var cropMode: CropMode = CropMode.Fit, // 图片缩放模式
    override var alignment: Alignment = Alignment.CENTER,
    var samplingMode: SamplingMode = SamplingMode.DEFAULT, // 图片采样模式
    var paintBuilder: Paint.(element: ImageElement) -> Unit = {}, // 构建 Paint
): AbstractElement(), CropLayout, AlignmentLayout {
    init {
        elementDraw = buildDraw { context ->
            saveBlock({
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(size.width, size.height))
            }) {
                val pos = crop(image.size(), size.copy().minus(padding.size()))

                // 计算截取的区域
                val offset = alignment(image.size(), pos.size()).add(offset)

                imageShadowInfo?.let {
                    drawRect(size.copy().minus(padding.size()).rect(), paint {
                        imageFilter = it.getDropShadowImageFilterOnly()
                    })
                }
                saveBlock({
                    translate(padding.left, padding.top)
                    clipRect(size.copy().minus(padding.size()).rect())
                }) {

                    drawImageRect(
                        image,
                        Rect.makeXYWH(offset.x, offset.y, pos.width, pos.height),
                        size.copy().minus(padding.size()).rect(),
                        samplingMode,
                        buildPaint(),
                        true
                    )
                }
            }
        }
    }

    /**
     * 实时构建 Paint
     */
    private fun buildPaint(): Paint = paint {
        paintBuilder(this@ImageElement)
    }

    /**
     * 重写自动大小
     */
    override fun autoSize(): FloatSize {
        // 如果有最大化，向父组件请求大小，然后等比缩放
        var size = FloatSize(image.width, image.height)
        // 请求最大大小
        val maxSize = parent?.subElementMaxSize(this) ?: FloatSize()

        if (sizeMode.decode().contains(ElementSizeMode.MaxWidth) or sizeMode.decode().contains(ElementSizeMode.MaxHeight)){
            // 计算能完全容纳图像的大小
            val scaleW = maxSize.width / image.width
            val scaleH = maxSize.height / image.height

            val scale = if (scaleW == 0f) scaleH else if (scaleH == 0f) scaleW else minOf(scaleW, scaleH)
            // 计算自动大小
            size *= scale
        }

        return size.add(padding.size())
    }

}