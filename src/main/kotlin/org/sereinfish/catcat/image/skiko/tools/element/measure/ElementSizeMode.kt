package org.sereinfish.catcat.image.skiko.tools.element.measure

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

/**
 * 元素的大小模式
 *
 */
@JvmInline
value class ElementSizeMode internal constructor(
    private val value: Int
) {
    companion object{
        val Auto =          ElementSizeMode(0b001001) // 默认值，元素大小自动计算，超过父布局裁剪显示，没超过根据元素大小显示
        val AutoWidth =     ElementSizeMode(0b000001) // 自动宽度
        val AutoHeight =    ElementSizeMode(0b001000) // 自动高度
        val Value =         ElementSizeMode(0b010010) // 根据手动设置的大小显示
        val ValueWidth =    ElementSizeMode(0b000010)
        val ValueHeight =   ElementSizeMode(0b010000)
        val MaxFill =       ElementSizeMode(0b100100) // 占满父布局
        val MaxWidth =      ElementSizeMode(0b000100) // 最大宽度
        val MaxHeight =     ElementSizeMode(0b100000) // 最大高度
    }

    infix fun and(other: ElementSizeMode): ElementSizeMode {
        val widthFlag = (other.value and 0b000111).let { if (it == 0) value and 0b000111 else it }
        val heightFlag = (other.value and 0b111000).let { if (it == 0) value and 0b111000 else it }
        return ElementSizeMode(widthFlag or heightFlag)
    }

    infix fun not(other: ElementSizeMode): ElementSizeMode {
        return ElementSizeMode(value.and(other.value.inv()))
    }

    fun computeSize(autoSize: FloatSize, valueSize: FloatSize, maxSize: FloatSize): FloatSize {
        val widthFlag = value and 0b000111
        val heightFlag = value and 0b111000 shr 3
        val width = autoSize.width * (widthFlag and 1) + valueSize.width * (widthFlag shr 1 and 1)  + maxSize.width * (widthFlag shr 2)
        val height = autoSize.height * (heightFlag and 1) + valueSize.height * (heightFlag shr 1 and 1)  + maxSize.height * (heightFlag shr 2)
        return FloatSize(width = width, height = height)
    }

    /**
     * 包含某个模式
     */
    fun contain(mode: ElementSizeMode): Boolean {
        return value and mode.value != 0
    }

    override fun toString(): String {
        return "ElementSizeMode(value=${value.toString(2)})"
    }

}
