package org.sereinfish.catcat.image.skiko.tools.utils

import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.Rect

fun Rect.size() = FloatSize(right - left, bottom - top)
fun Rect.center() = FloatOffset(left + (right - left) / 2, top + (bottom - top) / 2)