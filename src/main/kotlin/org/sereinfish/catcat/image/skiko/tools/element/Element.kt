package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementAttrContext
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.sereinfish.catcat.image.skiko.tools.element.draw.ElementDrawChain
import org.sereinfish.catcat.image.skiko.tools.element.measure.SizeLayout
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

/**
 * 元素接口
 *
 * 定义元素基本行为和参数
 * 元素在绘制期间不应当继续测量自身，在绘制前完成自身测量
 *
 * 一个元素应该具备以下行为
 * - 测量自身大小
 * - 绘制自身
 * 以下参数
 * -
 */
interface Element: SizeLayout {
    var attributes: ElementAttrContext // 元素的属性
    // 元素绘制器
    var beforeDrawChain: ElementDrawChain // 前置绘制器
    var elementDraw: Draw // 元素绘制器
    var afterDrawChain: ElementDrawChain // 后置绘制器
    var parent: Layout? // 父元素

    /**
     * 元素的宽高
     */
    fun height(): Float
    fun width(): Float

    /**
     * 更新元素的信息
     * 计算元素的一些属性
     */
    fun updateElementInfo()

    /**
     * 更新元素大小
     */
    fun updateSize() {
        size = size()
    }

    fun updateWidth(){
        width = width()
    }
    fun updateHeight(){
        height = height()
    }

    /**
     * 传入绘制上下文，绘制此元素
     * 如果为顶层元素，绘制上下文由构建类定义
     *
     * 默认实现为先进行前置链绘制，然后绘制自己，然后绘制后置链
     */
    fun draw(context: ElementDrawContext){
        // 区域限制
        context.canvas.saveBlock({
            clipRect(Rect.makeXYWH(attributes.offset.x, attributes.offset.y, size.width, size.height))
            translate(attributes.offset.x, attributes.offset.y)
        }) {
            beforeDrawChain.draw(context.canvas, context)
            elementDraw.draw(context.canvas, context)
            afterDrawChain.draw(context.canvas, context)
        }
    }
}