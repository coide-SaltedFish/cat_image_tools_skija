package element.measure

import element.measure.size.FloatSize

interface SizeLayout {
    var size: FloatSize
    var sizeMode: ElementSizeMode

    /**
     * 元素最大大小
     */
    fun maxSize(): FloatSize

    /**
     * 大小的默认实现
     */
    fun size(): FloatSize
}