package utils

import element.measure.offset.FloatOffset
import element.measure.size.FloatSize
import org.jetbrains.skia.Rect

fun Rect.size() = FloatSize(right - left, bottom - top)
fun Rect.center() = FloatOffset(left + (right - left) / 2, top + (bottom - top) / 2)