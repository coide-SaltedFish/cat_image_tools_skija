package org.sereinfish.catcat.image.skiko.tools.draw.effect

import org.jetbrains.skia.Path

/**
 * 形状接口
 */
interface Shape {
    fun path(): Path // 形状
}