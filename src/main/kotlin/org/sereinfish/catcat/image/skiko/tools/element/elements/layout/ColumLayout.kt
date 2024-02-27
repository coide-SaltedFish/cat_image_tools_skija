package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.*
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

    override fun width(): Float = subElements.maxOf { it.width } + padding.width
    override fun height(): Float = subElements.sumOf { it.height } + padding.height

    /**
     * 获取子元素Y坐标
     */
    private fun getSubElementY(element: Element): Float =
        subElements.sumOrEnd({ it == element }){
            it.size.height
        }

    /**
     * 更新布局大小
     * 1. 更新除Max子元素宽度
     * 2. 更新布局宽度
     * 3. 更新子元素高度
     * 4. 更新布局高度
     */
    override fun updateSize() {
        initSizeFlag()

        if (sizeMode.contain(ElementSizeMode.AutoHeight).not()) updateHeight()
        if (sizeMode.contain(ElementSizeMode.AutoWidth).not()) updateWidth()

        val (max, nonMax) = subElements.partition { it.sizeMode.contain(ElementSizeMode.MaxWidth) }
        // 更新子元素宽度
        nonMax.forEach { it.updateWidth() }
        updateWidth()

        max.forEach { it.updateWidth() }

        // 更新子元素高度
        subElements.forEach { it.updateHeight() }

        updateHeight()

        super<AbstractLayout>.updateSize()
        super<WeightLayout>.updateSize()

        subElements.forEach { it.updateSize() }
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
    override fun subElementMaxWidth(element: Element): Float {
        var w = width - padding.width
        if (element.sizeMode.contain(ElementSizeMode.MaxWidth) && weightSum.width != 0f){
            w = subWeightWidth(element)
        }

        return maxOf(w, 0f)
    }

    override fun subElementMaxHeight(element: Element): Float {
        var h = height - padding.height
        subElements.forEachOrEnd({ it == element && size.height > 0 }){
            h -= it.size.height
        }
        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoHeight))
            h = 0f
        else {
            if (element.sizeMode.contain(ElementSizeMode.MaxHeight) && weightSum.height != 0f) {
                h = subWeightHeight(element)
            }
        }

        return maxOf(h, 0f)
    }
}