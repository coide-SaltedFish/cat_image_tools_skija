package org.sereinfish.catcat.image.skiko.tools

import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.colum
import org.sereinfish.catcat.image.skiko.tools.build.extend.rect
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout

fun <T: Element> T.size(w: Number, h: Number): T {
    size(w, h)
    return this
}

fun <T: AbstractElement> T.padding(value: Number): T {
    padding(value)
    return this
}

fun <T: Layout> T.scope(block: T.() -> Unit): T {
    this.block()
    return this
}

fun main() {
    buildImageColumLayout {
        colum()
            .size(20 ,20)
            .padding(20)
            .scope {
                rect {

                }.size(60, 60)
            }

    }
}