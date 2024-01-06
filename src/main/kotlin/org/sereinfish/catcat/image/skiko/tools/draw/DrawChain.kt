package org.sereinfish.catcat.image.skiko.tools.draw

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.jetbrains.skia.Canvas
import java.util.LinkedList

/**
 * 绘制链
 *
 * 不能进行直接实例化，应该根据需要进行自定义的继承实现
 */
abstract class DrawChain<T: Draw> {
    val draws = LinkedList<T>()

    operator fun plus(draw: T): DrawChain<T> {
        draws.add(draw)
        return this
    }

    /**
     * 遍历绘制器列表
     */
    inline fun forEach(block: (draw: T) -> Unit){
        draws.forEach { block(it) }
    }

    /**
     * 进行链式绘制调用
     */
    fun draw(canvas: Canvas, context: Context){
        draws.forEach { it.draw(canvas, context) }
    }
}