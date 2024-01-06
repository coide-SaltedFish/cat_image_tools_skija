package org.sereinfish.catcat.image.skiko.tools.draw.utils

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.jetbrains.skia.Canvas

/**
 * 快速构建一个匿名的绘制器对象
 */
fun buildDraw(block: Canvas.(context: Context) -> Unit): Draw {
    return object : Draw {
        override fun draw(canvas: Canvas, context: Context) {
            block(canvas, context)
        }
    }
}