package element.elements.layout

import element.AbstractLayout
import element.Element
import element.measure.ElementSizeMode
import element.measure.alignment.Alignment
import element.measure.alignment.AlignmentLayout
import element.measure.offset.FloatOffset
import element.measure.size.FloatSize
import utils.forEachOrEnd
import utils.sumOf
import utils.sumOrEnd

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
        val size = size.copy().minus(FloatSize(padding.left, padding.top))
        val subElementsWidth by attributes.valueOrElse {
            var width = 0f
            for (subElement in subElements){
                if (subElement.sizeMode.contain(ElementSizeMode.MaxWidth)){
                    width += size.width - width
                    break
                }
                width += subElement.size().width
            }
            width
        }

        val subElementsHeight by attributes.valueOrElse { subElements.maxOf { it.size.height } }
        val allOffset = alignment(size, FloatSize(subElementsWidth, subElementsHeight)) // 全局偏移

        val subOffset = alignment(FloatSize(element.size.width, subElementsHeight), element.size) // 局部偏移

        val y = allOffset.y + subOffset.y
        return FloatOffset(allOffset.x + subElements.sumOrEnd({ it == element }){ it.size.width }, y)
    }

    override fun subElementMaxSize(element: Element): FloatSize {
        val size = size.copy().minus(FloatSize(padding.left, padding.top))
        subElements.forEachOrEnd({ it == element }){
            size.minus(it.size.copy(height = 0f))
        }

        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoWidth))
            size.width = 0f


        if (sizeMode.contain(ElementSizeMode.AutoHeight))
            size.height = this.size.height

        return size.nonNegativeValue()
    }
}