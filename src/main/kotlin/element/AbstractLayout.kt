package element

import element.context.ElementDrawContext
import element.elements.layout.RowLayout
import element.measure.offset.FloatOffset
import org.jetbrains.skia.Color
import org.jetbrains.skia.Rect
import utils.paint

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
        context.canvas.save()
        context.canvas.clipRect(Rect.makeXYWH(attributes.offset.x, attributes.offset.y, size.width, size.height))
        context.canvas.translate(attributes.offset.x, attributes.offset.y)

        beforeDrawChain.draw(context.canvas, context)
        elementDraw.draw(context.canvas, context)

        // 绘制子元素
        subElements.forEach { subElement ->
            subElement.draw(context)
        }

        afterDrawChain.draw(context.canvas, context)
        // 解除区域限制
        context.canvas.restore()
    }

    override fun updateElementInfo() {
        // 更新自己的数据
        size = size()
        super.updateElementInfo()

        // 更新子元素的数据
        subElements.forEach { it.updateElementInfo() }
    }
}