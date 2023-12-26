package utils

import org.jetbrains.skia.Canvas

/**
 * canvas的一个状态块
 */
fun Canvas.saveBlock(initBlock: Canvas.() -> Unit = {}, block: Canvas.() -> Unit) {
    save()
    initBlock()
    block()
    restore()
}