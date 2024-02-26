package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

/**
 * 一个具有一定默认实现的抽象布局类
 */
abstract class AbstractLayout(
    override val subElements: LinkedHashSet<Element> = LinkedHashSet()
): AbstractElement(), Layout {

    /**
     * 重写Draw方法，完成子元素绘制
     */
    override fun draw(context: ElementDrawContext) {
        // 区域限制
        context.canvas.saveBlock({
            clipRect(Rect.makeXYWH(attributes.offset.x, attributes.offset.y, size.width, size.height))
            translate(attributes.offset.x, attributes.offset.y)
        }) {
            beforeDrawChain.draw(context.canvas, context)
            elementDraw.draw(context.canvas, context)

            // 绘制子元素
            subElements.forEach { subElement ->
                saveBlock({
                    translate(padding.left, padding.top)
//                    translate(subElement.attributes.offset.x, subElement.attributes.offset.y)
                }) {
                    subElement.draw(context)
                }
            }


            afterDrawChain.draw(context.canvas, context)
        }
    }

    /**
     * 更新布局大小
     *
     * 1. 计算除开max大小的子元素大小
     * 2. 计算布局大小
     * 3. 计算max子元素大小
     */
    // abstract override fun updateSize()

    /**
     * 更新布局信息数据
     */
    override fun updateElementInfo() {
        // 更新自己的数据
        super.updateElementInfo()

        // 更新子元素的数据
        subElements.forEach { it.updateElementInfo() }
    }
}