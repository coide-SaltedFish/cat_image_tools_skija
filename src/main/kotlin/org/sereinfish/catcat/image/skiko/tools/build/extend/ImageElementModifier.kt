package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.draw.filter.makeEdgeExtension
import org.sereinfish.catcat.image.skiko.tools.element.elements.ImageElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter

/**
 * 图像模糊
 */
fun Modifier<ImageElement>.imageBlur(
    sigmaX: Number,
    sigmaY: Number,
) = modifier {
    val old = paintBuilder
    paintBuilder = {
        old(this@modifier)
        imageFilter = ImageFilter.makeCompose(
            imageFilter,
            ImageFilter.makeBlur(
                sigmaX.toFloat(),
                sigmaY.toFloat(),
                FilterTileMode.CLAMP,
                ImageFilter.makeEdgeExtension(this@modifier.size)
            )
        )
    }
}

fun Modifier<ImageElement>.imageBlur(
    radius: Number,
) = imageBlur(radius, radius)

/**
 * 图像阴影
 */
fun Modifier<ImageElement>.imageShadow(
    shadowInfo: ShadowInfo
) = modifier {
    this.imageShadowInfo = shadowInfo
}

fun Modifier<ImageElement>.imageShadow(
    dx: Number, dy: Number, radius: Number, color: Int
) = modifier {
    this.imageShadowInfo = ShadowInfo(dx, dy, radius, color)
}

fun Modifier<ImageElement>.imageShadow(
    d: Number, radius: Number, color: Int
) = modifier {
    this.imageShadowInfo = ShadowInfo(d, radius, color)
}

