package org.sereinfish.catcat.image.skiko.tools.build

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.jetbrains.skia.*

/**
 * 构建一个图片
 */
class ImageBuilder<T: Layout>(
    val layout: T = ColumLayout() as T, // 默认布局
    val modifier: Modifier<T>? = null,
    val colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
){
    /**
     * 构建图片
     */
    fun build(): Image {
        modifier?.modifier(element = layout)
        layout.updateElementInfo()
        val surface = Surface.makeRaster(ImageInfo(colorInfo, layout.size.width.toInt(), layout.size.height.toInt()))
        layout.draw(ElementDrawContext(surface))
        return surface.makeImageSnapshot()
    }
}

/**
 * 构建图片
 */
fun buildImage(
    modifier: Modifier<Layout>? = null,
    layout: Layout = ColumLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    block: Layout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo)
    builder.layout.block()
    return builder.build()
}

fun buildImageColumLayout(
    modifier: Modifier<ColumLayout>? = null,
    layout: ColumLayout = ColumLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    block: ColumLayout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo)
    builder.layout.block()
    return builder.build()
}

fun buildImageRowLayout(
    modifier: Modifier<RowLayout>? = null,
    layout: RowLayout = RowLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    block: RowLayout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo)
    builder.layout.block()
    return builder.build()
}
