package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementAttrContext
import org.sereinfish.catcat.image.skiko.tools.element.draw.ElementDrawChain
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import kotlin.math.max

abstract class AbstractElement(
    override var parent: Layout? = null, // 父元素
    override var beforeDrawChain: ElementDrawChain = ElementDrawChain(), // 前置绘制器
    override var elementDraw: Draw = buildDraw { _ -> }, // 元素绘制器
    override var afterDrawChain: ElementDrawChain = ElementDrawChain(), // 后置绘制器
): Element {
    final override var attributes: ElementAttrContext = ElementAttrContext()
    override var size: FloatSize by attributes.valueOrElse { FloatSize() } // 这个大小来自缓存
    override var sizeMode: ElementSizeMode by attributes.valueOrElse { ElementSizeMode.Auto }

    var padding by attributes.valueOrDefault(FloatRectSize())

    var minSize by attributes.valueOrNull<FloatSize>() // 最小大小
    var maxSize by attributes.valueOrNull<FloatSize>() // 最大大小

    /**
     * 获取元素的大小
     *
     * 这个大小是实时计算的
     */
    override fun size(): FloatSize {
        val size = FloatSize()
        val modes = sizeMode.decode()

        val autoSize = autoSize()
        val valueSize = this.size
        val maxSize = maxSize()

        for (mode in modes){
            when(mode){
                ElementSizeMode.AutoWidth -> size.width = autoSize.width
                ElementSizeMode.AutoHeight -> size.height = autoSize.height

                ElementSizeMode.ValueWidth -> size.width = valueSize.width
                ElementSizeMode.ValueHeight -> size.height = valueSize.height

                ElementSizeMode.MaxHeight -> size.height = maxSize.height
                ElementSizeMode.MaxWidth -> size.width = maxSize.width
            }
        }
        // 进行最大最小大小计算
        this.maxSize?.let {
            if (it.width != 0f) size.width = minOf(it.width, size.width)
            if (it.height != 0f) size.height = minOf(it.height, size.height)
        }

        this.minSize?.let {
            if (it.width != 0f) size.width = maxOf(it.width, size.width)
            if (it.height != 0f) size.height = maxOf(it.height, size.height)
        }

        return size
    }

    /**
     * 获取元素最大大小
     */
    override fun maxSize(): FloatSize {
        return parent?.subElementMaxSize(this) ?: FloatSize()
    }

    /**
     * 基本实现
     */
    override fun updateElementInfo() {
        // 计算大小
        size = size()

        val parentPadding: FloatRectSize = parent?.attributes?.get("padding") as? FloatRectSize ?: FloatRectSize()
        // 计算相对坐标
        attributes.offset = parent?.subElementOffset(this)?.copy() ?: FloatOffset()

        // 计算绝对坐标
        attributes.offsetAbsolute = parent?.attributes?.offsetAbsolute?.copy()?.add(attributes.offset) ?: attributes.offset.copy()
        attributes.offsetAbsolute.add(parentPadding.offset())
    }
}