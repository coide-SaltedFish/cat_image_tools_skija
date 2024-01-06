package org.sereinfish.catcat.image.skiko.tools

import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.utils.offset


class A {
    val p: A get() = this

    operator fun get(x: Number, y: Number): FloatOffset {
        return offset(x, y)
    }
}
fun main() {
    val func: A.() -> FloatOffset = {
        p[1, 2]
    }
}