package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.AbstractLayout
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import kotlin.math.max

/**
 * 绝对布局
 */
open class AbsoluteLayout: AbstractLayout(), AlignmentLayout {
    @Deprecated("此参数无效")
    override var alignment: Alignment = Alignment.TOP // 实际上无用

    /**
     * 子元素位置信息
     */
    private val subElementOffsetInfo = LinkedHashMap<Element, OffsetInfo>()

    override fun add(element: Element): Layout {
        super.add(element)
        subElementOffsetInfo[element] = OffsetInfo()
        return this
    }

    /**
     * 提供单独的子元素添加方法
     */
    fun add(element: Element, offsetInfo: OffsetInfo): Layout {
        super.add(element)
        subElementOffsetInfo[element] = offsetInfo
        return this
    }

    override fun width(): Float =
        subElements.maxOfOrNull { it.size.width + (subElementOffsetInfo[it]?.offset?.x ?: 0f) } ?: 0f

    override fun height(): Float =
        subElements.maxOfOrNull { it.size.height + (subElementOffsetInfo[it]?.offset?.y ?: 0f) } ?: 0f

    /**
     * 绝对布局更新大小
     *
     * 1. 更新非max子元素大小
     * 2. 更新自己的大小
     * 3. 更新max子元素大小
     */
    override fun updateSize() {
        initSizeFlag()

        if (sizeMode.contain(ElementSizeMode.AutoHeight).not()) updateHeight()
        if (sizeMode.contain(ElementSizeMode.AutoWidth).not()) updateWidth()

        val (maxW, nonMaxW) = subElements.partition { sizeMode.contain(ElementSizeMode.MaxWidth) }
        val (maxH, nonMaxH) = subElements.partition { sizeMode.contain(ElementSizeMode.MaxHeight) }
        nonMaxW.forEach { it.updateWidth() }
        nonMaxH.forEach { it.updateHeight() }
        subElements.forEach { it.updateSize() }

        super.updateSize()

        maxW.forEach { it.updateWidth() }
        maxH.forEach { it.updateHeight() }

        subElements.forEach { it.updateSize() }
    }

    /**
     * 获取子元素位置
     * 如果子元素有对齐参数，返回子元素对齐后位置，反之返回子元素预设位置
     */
    override fun subElementOffset(element: Element): FloatOffset {
        return subElementOffsetInfo[element]?.let { info ->
            info.alignment?.let {
                alignment(it, size.copy().minus(padding.size()), element.size.copy())
            } ?: info.offset
        } ?: FloatOffset()
    }

    /**
     * 返回布局大小
     */
    override fun subElementMaxWidth(element: Element): Float {
        var w = width - padding.width
        val offset = subElementOffsetInfo[element]?.offset ?: FloatOffset()
        w -= offset.x
        w -= padding.width

        return maxOf(w, 0f)
    }

    override fun subElementMaxHeight(element: Element): Float {
        var h = height - padding.height
        h -= subElementOffsetInfo[element]?.offset?.y ?: 0f
        h -= padding.height

        return maxOf(h, 0f)
    }

    /**
     * 位置信息
     */
    data class OffsetInfo(
        var offset: FloatOffset = FloatOffset(),
        var alignment: Alignment? = null,
    )
}