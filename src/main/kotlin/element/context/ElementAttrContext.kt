package element.context

import context.Context
import element.measure.ElementSizeMode
import element.measure.offset.FloatOffset
import element.measure.size.FloatRectSize
import element.measure.size.FloatSize

/**
 * 元素属性上下文
 */
class ElementAttrContext: Context() {
    var weight: Float by valueOrDefault(0f) // 元素的占比
    var padding: FloatRectSize by valueOrDefault(FloatRectSize()) // 元素内边距
    var offset: FloatOffset by valueOrDefault(FloatOffset()) // 元素的相对位置, 不包含内边距
    var offsetAbsolute: FloatOffset by valueOrDefault(FloatOffset()) // 元素的绝对位置，包含内边距
}