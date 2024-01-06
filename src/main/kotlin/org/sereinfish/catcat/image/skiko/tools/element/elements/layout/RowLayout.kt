package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.AbstractLayout
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.forEachOrEnd
import org.sereinfish.catcat.image.skiko.tools.utils.sumOrEnd

class RowLayout(
    override var alignment: Alignment = Alignment.LEFT
): AbstractLayout(), AlignmentLayout {

    override fun autoSize(): FloatSize {
        return FloatSize().apply {
            subElements.forEach {
                val subSize = it.size()
                width += subSize.width
                height = maxOf(subSize.height, height)
            }
        }.add(padding.size())
    }

    override fun subElementOffset(element: Element): FloatOffset {
        val size = size.copy().minus(padding.size()) // 布局能提供的空间

        val subElementsWidth by attributes.valueOrElse { // 所有子元素宽度
            var width = 0f
            for (subElement in subElements){
                if (subElement.sizeMode.contain(ElementSizeMode.MaxWidth)){
                    width = size.width
                    break
                }
                width += subElement.size().width
            }
            width
        }

        val subElementsHeight by attributes.valueOrElse { subElements.maxOf { it.size.height } } // 所有子元素高度

        val allOffset = alignment(size, FloatSize(subElementsWidth, subElementsHeight)) // 全局偏移

        val subOffset = alignment(FloatSize(element.size.width, subElementsHeight), element.size) // 局部偏移

        val y = allOffset.y + subOffset.y
        // 计算已计算子元素宽度，然后加上默认偏移
        val x = allOffset.x + subElements.sumOrEnd({ it == element }){ it.size.width }
        return FloatOffset(x, y)
    }

    override fun subElementMaxSize(element: Element): FloatSize {
        val size = size.copy().minus(padding.size())
        subElements.forEachOrEnd({ it == element }){
            size.minus(it.size.copy(height = 0f))
        }

        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoWidth))
            size.width = 0f


        if (sizeMode.contain(ElementSizeMode.AutoHeight))
            size.height = this.size.height - padding.size().height

        return size.nonNegativeValue()
    }
}