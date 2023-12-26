package element

import draw.Draw
import draw.utils.buildDraw
import element.context.ElementAttrContext
import element.draw.ElementDrawChain
import element.elements.TextElement
import element.elements.layout.ColumLayout
import element.elements.layout.RowLayout
import element.measure.ElementSizeMode
import element.measure.offset.FloatOffset
import element.measure.size.FloatRectSize
import element.measure.size.FloatSize

abstract class AbstractElement(
    override var parent: Layout? = null, // 父元素
    override var beforeDrawChain: ElementDrawChain = ElementDrawChain(), // 前置绘制器
    override var elementDraw: Draw = buildDraw { _ -> }, // 元素绘制器
    override var afterDrawChain: ElementDrawChain  = ElementDrawChain(), // 后置绘制器
): Element {
    final override var attributes: ElementAttrContext = ElementAttrContext()
    override var size: FloatSize by attributes.valueOrElse { FloatSize() } // 这个大小来自缓存
    override var sizeMode: ElementSizeMode by attributes.valueOrElse { ElementSizeMode.Auto }

    var padding by attributes.valueOrDefault(FloatRectSize())

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

        attributes.offset = attributes.offset.add(parentPadding.offset())

        // 计算绝对坐标
        attributes.offsetAbsolute = parent?.attributes?.offsetAbsolute?.copy()?.add(attributes.offset) ?: attributes.offset.copy()
    }
}