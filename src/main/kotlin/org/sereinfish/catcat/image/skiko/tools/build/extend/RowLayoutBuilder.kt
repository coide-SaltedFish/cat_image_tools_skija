package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment

fun Layout.row(
    modifier: Modifier<in RowLayout> = Modifier(),
    alignment: Alignment = Alignment.LEFT,
    block: RowLayout.() -> Unit = {}
) = RowLayout(alignment).also {
    modifier.modifier(it)
    it.block()
    add(it)
}