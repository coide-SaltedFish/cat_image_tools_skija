package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.AbsoluteLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.utils.offset


/**
 * 绝对布局子元素构建器
 */
class AbsoluteLayoutSubElementBuilder: AbsoluteLayout() {
    val TOP = Alignment.TOP // 顶部对齐
    val BOTTOM = Alignment.BOTTOM // 底部对齐
    val LEFT = Alignment.LEFT // 左侧对齐
    val RIGHT = Alignment.RIGHT // 右侧对齐
    val CENTER = Alignment.CENTER // 整体居中
    val CENTER_HORIZONTAL = Alignment.CENTER_HORIZONTAL // 水平居中
    val CENTER_VERTICAL = Alignment.CENTER_VERTICAL // 垂直居中

    @Deprecated("在DSL写法中，绝对布局不对此方法进行支持", ReplaceWith("this"))
    override fun add(element: Element): Layout {
        return this
    }

    val p: AbsoluteLayoutSubElementBuilder get() = this

    operator fun get(x: Number, y: Number) = offset(x, y)

    /**
     * + element
     */
    operator fun Element.unaryPlus(): AbsoluteLayoutSubElementBuilder {
        add(this, OffsetInfo())
        return this@AbsoluteLayoutSubElementBuilder
    }

    infix fun place(element: Element) {
        add(element, OffsetInfo())
    }

    infix fun Element.on(builder: AbsoluteLayoutSubElementBuilder) {
        add(this, OffsetInfo())
    }

    /**
     * 添加子元素到指定位置
     */
    infix fun FloatOffset.place(element: Element) {
        add(element, OffsetInfo(this))
    }

    infix fun Element.on(offset: FloatOffset) {
        add(this, OffsetInfo(offset))
    }

    infix fun Alignment.place(element: Element) {
        add(element, OffsetInfo(alignment = this))
    }

    infix fun Element.on(alignment: Alignment) {
        add(this, OffsetInfo(alignment = alignment))
    }
}
fun Layout.abs(
    modifier: Modifier<in AbsoluteLayout> = Modifier(),
    block: AbsoluteLayoutSubElementBuilder.() -> Unit
): AbsoluteLayout {
    val layout = AbsoluteLayoutSubElementBuilder()
    modifier.modifier(layout)
    layout.block()

    add(layout)
    return layout
}