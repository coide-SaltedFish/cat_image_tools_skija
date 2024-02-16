package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.AbstractLayout
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.WeightLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.forEachOrEnd
import org.sereinfish.catcat.image.skiko.tools.utils.sumOf
import org.sereinfish.catcat.image.skiko.tools.utils.sumOrEnd

/**
 * 垂直布局
 */
open class ColumLayout(
    override var alignment: Alignment = Alignment.LEFT.and(Alignment.TOP),
): AbstractLayout(), AlignmentLayout, WeightLayout {

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
    private fun getSubElementY(element: Element): Float =
        subElements.sumOrEnd({ it == element }){
            it.size().height
        }

    override fun updateElementInfo() {
        super<WeightLayout>.updateElementInfo()
        super<AbstractLayout>.updateElementInfo()
    }

    /**
     * 计算子元素坐标
     */
    override fun subElementOffset(element: Element): FloatOffset {
        val size = size.copy().minus(padding.size()) // 父元素能提供的最大空间
        val subElementsHeight: Float by attributes.valueOrElse { // 计算所有子元素高度
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
        val subElementsWidth: Float by attributes.valueOrElse { subElements.maxOf { it.size.width } } // 所有子元素的宽度

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

        if (element.sizeMode.contain(ElementSizeMode.MaxHeight) && weightSum != 0f){
            size.height = subElementWeightSize(element).height
        }

        return size.nonNegativeValue()
    }
}