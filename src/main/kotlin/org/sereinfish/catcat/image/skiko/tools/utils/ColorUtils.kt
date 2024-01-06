package org.sereinfish.catcat.image.skiko.tools.utils

import org.jetbrains.skia.Color

fun Int.colorCopy(r: Int? = null, g: Int? = null, b: Int? = null, a: Int? = null): Int {
    val nr = r ?: Color.getR(this)
    val ng = g ?: Color.getG(this)
    val nb = b ?: Color.getB(this)
    val na = a ?: Color.getA(this)

    return Color.makeARGB(na, nr, ng, nb)
}