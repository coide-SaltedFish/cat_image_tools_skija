package element.elements.layout

import element.AbstractLayout
import element.Element
import element.elements.TextElement
import element.measure.ElementSizeMode
import element.measure.ElementSizeMode.*
import element.measure.alignment.Alignment
import element.measure.alignment.AlignmentLayout
import element.measure.offset.FloatOffset
import element.measure.size.FloatSize
import utils.forEachOrEnd
import utils.sumOf

/**
 * 垂直布局
 */
class ColumLayout(
    override var alignment: Alignment = Alignment.LEFT.and(Alignment.TOP),
): AbstractLayout(), AlignmentLayout {

    /**
     * 自动大小
     */
    override fun autoSize(): FloatSize {
        return FloatSize().apply {
            subElements.forEach {
                val subSize = it.size()
                height += subSize.height
                width = maxOf(width, subSize.width)
            }
        }.add(padding.size())
    }

    /**
     * 获取子元素Y坐标
     */
    private fun getSubElementY(element: Element): Float {
        var y = padding.top

        subElements.forEachOrEnd({ it == element }){
            y += it.size().height
        }

        return y
    }

    /**
     * 计算子元素坐标
     */
    override fun subElementOffset(element: Element): FloatOffset {
        val size = size.copy().minus(padding.size())
        val subElementsHeight: Float by attributes.valueOrElse {
            var sum = 0f
            for (subElement in subElements){
                if (subElement.sizeMode.contain(ElementSizeMode.MaxHeight)) {
                    sum = size.height
                    break
                }
                sum += subElement.size().height
            }
            sum
        }
        val subElementsWidth: Float by attributes.valueOrElse { subElements.maxOf { it.size.width } }

        val allOffset = alignment(size, FloatSize(subElementsWidth, subElementsHeight)) // 全局偏移

        val subOffset = alignment(FloatSize(subElementsWidth, element.size.height), element.size) // 局部偏移

        val x = allOffset.x + subOffset.x

        return FloatOffset(x, allOffset.y + getSubElementY(element))
    }

    /**
     * 返回子元素所能拥有最大空间
     */
    override fun subElementMaxSize(element: Element): FloatSize {
        val size = size.copy().minus(padding.size())
        subElements.forEachOrEnd({ it == element }){
            size.minus(it.size.copy(width = 0f))
        }


        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoHeight))
            size.height = 0f

        return size.nonNegativeValue()
    }
}