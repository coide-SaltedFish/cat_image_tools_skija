package org.sereinfish.catcat.image.skiko.tools.element.measure

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

interface SizeLayout {
    var size: FloatSize
    var sizeMode: ElementSizeMode

    /**
     * 元素最大大小
     */
    fun maxSize(): FloatSize

    /**
     * 大小的默认实现
     *
     * 通常来说，每个元素的这个方法在每次绘制时只应该调用一次
     */
    fun size(): FloatSize
}