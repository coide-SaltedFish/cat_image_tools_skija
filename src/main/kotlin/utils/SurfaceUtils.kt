package utils

import element.measure.size.FloatSize
import org.jetbrains.skia.Surface

fun Surface.size() = FloatSize(width, height)