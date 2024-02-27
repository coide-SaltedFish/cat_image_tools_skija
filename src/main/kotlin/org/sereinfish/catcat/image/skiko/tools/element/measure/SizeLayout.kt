package org.sereinfish.catcat.image.skiko.tools.element.measure

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

interface SizeLayout {
    var size: FloatSize
    var sizeMode: ElementSizeMode

    var width: Float
        get() = size.width
        set(value) { size.width = value }

    var height: Float
        get() = size.height
        set(value) { size.height = value }

    /**
     * 大小的默认实现
     *
     * 通常来说，每个元素的这个方法在每次绘制时只应该调用一次
     */
    fun size(): FloatSize
}