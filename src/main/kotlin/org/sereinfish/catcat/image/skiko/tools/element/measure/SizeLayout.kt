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
     */
    fun size(): FloatSize
}