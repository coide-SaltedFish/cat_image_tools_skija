package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and

fun Layout.colum(
    modifier: Modifier<ColumLayout>? = null,
    alignment: Alignment = org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment.LEFT.and(org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment.TOP),
    block: ColumLayout.() -> Unit = {}
): ColumLayout {
    val layout = ColumLayout(alignment)
    modifier?.modifier(layout)
    layout.block()

    add(layout)
    return layout
}
