package org.sereinfish.catcat.image.skiko.tools.element.measure.alignment

import kotlin.math.abs

/**
 * 对齐方式
 */
open class Alignment(val value: Int) {

    companion object {
        val TOP = Alignment(0x1) // 顶部对齐
        val BOTTOM = Alignment(0x2) // 底部对齐
        val LEFT = Alignment(0x4) // 左侧对齐
        val RIGHT = Alignment(0x8) // 右侧对齐
        val CENTER = Alignment(0x10) // 整体居中
        val CENTER_HORIZONTAL = Alignment(0x20) // 水平居中
        val CENTER_VERTICAL = Alignment(0x40) // 垂直居中
    }

    /**
     * 解码组合的对齐方式
     */
    fun decode(): List<Alignment> {
        var v = abs(value)
        var step = 0
        return buildList {
            while (v > 0){
                if (v.and(0x1) == 0x1){
                    add(Alignment(0x1.shl(step)))
                }
                v = v.shr(1)
                step ++
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Alignment

        return value == other.value
    }

    override fun toString(): String {
        return "Alignment(value=${value.toString(2)})"
    }


}

/**
 * 返回一个组合式对齐方式
 */
infix fun Alignment.and(alignment: Alignment): Alignment {
    return Alignment(this.value.or(alignment.value))
}