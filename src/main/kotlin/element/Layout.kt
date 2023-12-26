package element

import element.measure.offset.FloatOffset
import element.measure.size.FloatSize

/**
 * 布局接口
 *
 * 布局需要容纳元素，布局也是元素
 *
 * 布局应当向子元素提供以下基准测量方法：
 * - 子元素的绘制位置
 * - 元素可使用最大宽度，元素可使用最大高度。
 *
 * 注：布局需要完成子元素的计算和绘制
 */
interface Layout: Element {
    val subElements: LinkedHashSet<Element> // 子元素集合

    /**
     * 添加子元素
     */
    fun add(element: Element): Layout {
        if (element.parent != null && element.parent != this) {
            TODO("发起警告，子元素已有父布局")
        }

        element.parent = this
        subElements.add(element)
        return this
    }

    /**
     * 子元素绘制坐标
     *
     * 返回元素的相对坐标
     */
    fun subElementOffset(element: Element): FloatOffset

    /**
     * 子元素可使用最大大小
     */
    fun subElementMaxSize(element: Element): FloatSize
}