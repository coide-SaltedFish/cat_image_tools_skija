package org.sereinfish.catcat.image.skiko.tools.draw.draws

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.size

/**
 * 在元素上绘制一个图片
 */
class ElementImageDraw(
    val layoutSize: FloatSize,
    val image: Image,
    var samplingMode: SamplingMode = SamplingMode.DEFAULT, // 图片采样模式
    override var cropMode: CropMode = CropMode.Fit,
    override var alignment: Alignment = Alignment.CENTER,
    var paintBuilder: Paint.() -> Unit = {}, // 构建 Paint
): Draw, CropLayout, AlignmentLayout {
    override fun draw(canvas: Canvas, context: Context) {
        val pos = crop(layoutSize, image.size())
        val offset = alignment(image.size(), pos.size())
        canvas.drawImageRect(
            image,
            Rect.makeXYWH(offset.x, offset.y, pos.width, pos.height),
            layoutSize.copy().rect(),
            samplingMode,
            paint(paintBuilder),
            true
        )
    }
}