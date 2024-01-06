package org.sereinfish.catcat.image.skiko.tools.element.context

import org.sereinfish.catcat.image.skiko.tools.context.Context
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize

/**
 * 元素属性上下文
 */
class ElementAttrContext: Context() {
    var weight: Float by valueOrDefault(0f) // 元素的占比
    var padding: FloatRectSize by valueOrDefault(FloatRectSize()) // 元素内边距
    var offset: FloatOffset by valueOrDefault(FloatOffset()) // 元素的相对位置, 不包含内边距
    var offsetAbsolute: FloatOffset by valueOrDefault(FloatOffset()) // 元素的绝对位置，包含内边距
}