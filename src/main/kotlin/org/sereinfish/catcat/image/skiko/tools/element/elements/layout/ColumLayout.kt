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
                if (it.sizeMode.contain(ElementSizeMode.MaxHeight).not())
                    height += it.size.height
                if (it.sizeMode.contain(ElementSizeMode.MaxWidth).not())
                    width = maxOf(width, it.size.width)
            }
        }.add(padding.size())
    }

    /**
     * 获取子元素Y坐标
     */
    private fun getSubElementY(element: Element): Float =
        subElements.sumOrEnd({ it == element }){
            it.size.height
        }

    /**
     * 更新布局大小
     * 1. 更新子元素大小
     * 2. 更新自己大小
     * 3. 作为比例布局更新大小
     * 4. 再次更新max子元素大小
     */
    override fun updateSize() {
        if (sizeMode == ElementSizeMode.Value || sizeMode == ElementSizeMode.MaxFill){
            super<AbstractLayout>.updateSize()
            super<WeightLayout>.updateSize()

            subElements.forEach { it.updateSize() }
        }else {
            subElements.forEach { it.updateSize() }
            super<AbstractLayout>.updateSize()
            super<WeightLayout>.updateSize()
            subElements.filter { ElementSizeMode.MaxFill.contain(it.sizeMode) }.forEach { it.updateSize() }
        }
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
                sum += subElement.size.height
            }
            sum
        }
        val subElementsWidth: Float by attributes.valueOrElse { subElements.maxOf { it.size.width } } // 所有子元素的宽度

        val allOffset = alignment(size, FloatSize(subElementsWidth, subElementsHeight)) // 全局偏移

        val subOffset = alignment(FloatSize(subElementsWidth, element.size.height), element.size.copy()) // 局部偏移

        val x = allOffset.x + subOffset.x

        return FloatOffset(x, allOffset.y + getSubElementY(element))
    }

    /**
     * 返回子元素所能拥有最大空间
     */
    override fun subElementMaxSize(element: Element): FloatSize {
        val size = size.copy().minus(padding.size())
        subElements.forEachOrEnd({ it == element && size.height > 0 }){
            size.height -= it.size.height
        }

        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoHeight))
            size.height = 0f

        // 计算比例坐标
        if (element.sizeMode.contain(ElementSizeMode.MaxHeight) && weightSum.height != 0f){
            size.height = subElementWeightSize(element).height
        }

        if (element.sizeMode.contain(ElementSizeMode.MaxWidth) && weightSum.width != 0f){
            size.width = subElementWeightSize(element).width
        }

        return size.nonNegativeValue()
    }
}