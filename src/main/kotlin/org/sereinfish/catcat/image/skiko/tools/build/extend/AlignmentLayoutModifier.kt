package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout

/**
 * 设置对齐方式
 */
fun <T> Modifier<T>.alignment(alignment: Alignment): Modifier<T> where T : Element, T : AlignmentLayout = modifier {
    this.alignment = alignment
}