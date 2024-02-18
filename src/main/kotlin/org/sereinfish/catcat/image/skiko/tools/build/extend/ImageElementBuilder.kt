package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.ImageElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.jetbrains.skia.Image
import org.jetbrains.skia.Paint
import org.jetbrains.skia.SamplingMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset

/**
 * 图片元素
 */
fun Layout.image(
    modifier: Modifier<in ImageElement>? = null,
    image: Image,
    offset: FloatOffset = FloatOffset(),
    imageShadowInfo: ShadowInfo? = null,
    cropMode: CropMode = CropMode.Fit, // 图片缩放模式
    alignment: Alignment = Alignment.CENTER,
    samplingMode: SamplingMode = SamplingMode.DEFAULT, // 图片采样模式
    paintBuilder: Paint.(element: ImageElement) -> Unit = {}, // 构建 Paint
    block: ImageElement.() -> Unit = {}
): ImageElement {
    val imageElement = ImageElement(image, imageShadowInfo, offset, cropMode, alignment, samplingMode, paintBuilder)
    modifier?.modifier(imageElement)
    imageElement.block()

    add(imageElement)

    return imageElement
}