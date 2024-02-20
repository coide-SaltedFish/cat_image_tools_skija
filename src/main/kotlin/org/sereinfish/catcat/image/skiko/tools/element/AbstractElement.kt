package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementAttrContext
import org.sereinfish.catcat.image.skiko.tools.element.draw.ElementDrawChain
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import java.util.concurrent.ConcurrentHashMap

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

    // 大小计算扩展模块
    var sizeExtendModules = ConcurrentHashMap<String, (FloatSize) -> Unit>()

    /**
     * 获取元素的大小
     *
     * 这个大小是实时计算的
     */
    override fun size(): FloatSize {
        val autoSize = autoSize()
        val valueSize = this.size
        val maxSize = maxSize()

        val size = sizeMode.computeSize(autoSize, valueSize, maxSize)

        sizeExtendModules.values.forEach { it(size) }

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
