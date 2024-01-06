package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.jetbrains.skia.Font

/**
 * 设置字体
 */
fun Modifier<TextElement>.font(font: Font) = modifier {
    this.font = font
}

/**
 * 设置文本阴影
 */
fun Modifier<TextElement>.textShadow(shadowInfo: ShadowInfo) = modifier {
    this.shadow = shadowInfo
}

fun Modifier<TextElement>.textShadow(dx: Number, dy: Number, radius: Number, color: Int) = modifier {
    this.shadow = ShadowInfo(dx, dy, radius, color)
}

fun Modifier<TextElement>.textShadow(d: Number, radius: Number, color: Int) = modifier {
    this.shadow = ShadowInfo(d, radius, color)
}