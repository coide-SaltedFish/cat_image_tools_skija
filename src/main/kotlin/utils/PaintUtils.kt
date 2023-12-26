package utils

import org.jetbrains.skia.Paint

/**
 * 画笔快速构建
 */
fun paint(block: Paint.() -> Unit): Paint = Paint().apply(block)