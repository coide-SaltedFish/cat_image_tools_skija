package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.*
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.forEachOrEnd
import org.sereinfish.catcat.image.skiko.tools.utils.sumOf
import org.sereinfish.catcat.image.skiko.tools.utils.sumOrEnd

class RowLayout(
    override var alignment: Alignment = Alignment.LEFT
): AbstractLayout(), AlignmentLayout, WeightLayout {

    override fun width(): Float = subElements.sumOf { it.width } + padding.width
    override fun height(): Float = subElements.maxOf { it.height } + padding.height

    /**
     * 更新布局大小
     * 1. 更新除Max子元素高度
     * 2. 更新布局高度
     * 3. 更新子元素宽度
     * 4. 更新布局宽度
     */
    override fun updateSize() {
        initSizeFlag()

        if (sizeMode.contain(ElementSizeMode.AutoHeight).not()) updateHeight()
        if (sizeMode.contain(ElementSizeMode.AutoWidth).not()) updateWidth()

        val (max, nonMax) = subElements.partition { it.sizeMode.contain(ElementSizeMode.MaxHeight) }
        nonMax.forEach { it.updateHeight() }
        updateHeight()
        max.forEach { it.updateHeight() }

        subElements.forEach { it.updateWidth() }

        updateWidth()

        super<AbstractLayout>.updateSize()
        super<WeightLayout>.updateSize()

        subElements.forEach { it.updateSize() }
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
                width += subElement.size.width
            }
            width
        }

        val subElementsHeight by attributes.valueOrElse { subElements.maxOf { it.size.height } } // 所有子元素高度

        val allOffset = alignment(size, FloatSize(subElementsWidth, subElementsHeight)) // 全局偏移

        val subOffset = alignment(FloatSize(element.size.width, subElementsHeight), element.size.copy()) // 局部偏移

        val y = allOffset.y + subOffset.y
        // 计算已计算子元素宽度，然后加上默认偏移
        val x = allOffset.x + subElements.sumOrEnd({ it == element }){ it.size.width }
        return FloatOffset(x, y)
    }

    override fun subElementMaxWidth(element: Element): Float {
        var w = width - padding.width
        subElements.forEachOrEnd({ it == element }){
            w -= it.size.width
        }
        // 当布局为自动时，不支持最大化子元素
        if (sizeMode.contain(ElementSizeMode.AutoWidth))
            w = 0f
        else {
            if (element.sizeMode.contain(ElementSizeMode.MaxWidth) && weightSum.width != 0f){
                w = subWeightWidth(element)
            }
        }

        return maxOf(w, 0f)
    }

    override fun subElementMaxHeight(element: Element): Float {
        var h = height - padding.height
        if (element.sizeMode.contain(ElementSizeMode.MaxHeight) && weightSum.height != 0f){
            h = subWeightHeight(element)
        }

        return h
    }
}