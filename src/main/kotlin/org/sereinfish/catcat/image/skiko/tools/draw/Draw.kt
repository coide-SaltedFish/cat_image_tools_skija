package org.sereinfish.catcat.image.skiko.tools.draw

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.jetbrains.skia.Canvas

/**
 * 绘制器
 *
 * 用于对图像进行绘制
 * 接收图像上下文
 */
interface Draw {
    /**
     * 绘制实现方法
     */
    fun draw(canvas: Canvas, context: Context)
}