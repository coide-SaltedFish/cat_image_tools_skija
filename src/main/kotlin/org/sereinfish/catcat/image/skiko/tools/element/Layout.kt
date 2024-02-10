package org.sereinfish.catcat.image.skiko.tools.element

import mu.KotlinLogging
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.slf4j.Logger

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

private val logger: Logger = KotlinLogging.logger("Element")

interface Layout: Element {

    val subElements: LinkedHashSet<Element> // 子元素集合

    /**
     * 添加子元素
     */
    fun add(element: Element): Layout {
        if (element.parent != null && element.parent != this) {
            logger.warn(
                "元素（${element::class.java}）已拥有父元素(${element.parent?.let { it::class.java }})，" +
                    "当前已添加到新的父元素(${this::class.java})，可能会造成子元素在原有父元素的绘制错误"
            )
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