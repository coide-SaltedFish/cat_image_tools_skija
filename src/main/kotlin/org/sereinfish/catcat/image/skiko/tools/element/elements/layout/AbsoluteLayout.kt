package org.sereinfish.catcat.image.skiko.tools.element.elements.layout

import org.sereinfish.catcat.image.skiko.tools.element.AbstractLayout
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

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

    override fun autoSize(): FloatSize {
        return FloatSize(
            width = subElements.maxOfOrNull { it.size().width } ?: 0f,
            height = subElements.maxOfOrNull { it.size().height } ?: 0f
        ).add(padding.size())
    }

    /**
     * 获取子元素位置
     * 如果子元素有对齐参数，返回子元素对齐后位置，反之返回子元素预设位置
     */
    override fun subElementOffset(element: Element): FloatOffset {
        return subElementOffsetInfo[element]?.let { info ->
            info.alignment?.let {
                alignment(it, size.copy().minus(padding.size()), element.size)
            } ?: info.offset
        } ?: FloatOffset()
    }

    /**
     * 返回布局大小
     */
    override fun subElementMaxSize(element: Element): FloatSize {
        val offset = subElementOffsetInfo[element]?.offset ?: FloatOffset()
        return size.copy().apply {
            width -= offset.x
            height -= offset.y
        }.minus(padding.size()).nonNegativeValue()
    }

    /**
     * 位置信息
     */
    data class OffsetInfo(
        var offset: FloatOffset = FloatOffset(),
        var alignment: Alignment? = null,
    )
}