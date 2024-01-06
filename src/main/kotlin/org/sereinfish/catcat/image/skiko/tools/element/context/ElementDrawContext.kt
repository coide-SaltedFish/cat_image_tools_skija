package org.sereinfish.catcat.image.skiko.tools.element.context

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Surface

/**
 * 一个元素绘制上下文
 */
class ElementDrawContext(
    val surface: Surface
): Context() {
    val canvas: Canvas get() = surface.canvas
}