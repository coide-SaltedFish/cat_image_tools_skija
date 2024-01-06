package org.sereinfish.catcat.image.skiko.tools.build.builder.layout

import org.sereinfish.catcat.image.skiko.tools.element.AbstractLayout
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout

/**
 * 布局构建器
 */
abstract class LayoutBuilder: AbstractLayout() {

    @Deprecated("不应当在构建时进行子元素变更", ReplaceWith("this"))
    override fun add(element: Element): Layout {
        return this
    }
}