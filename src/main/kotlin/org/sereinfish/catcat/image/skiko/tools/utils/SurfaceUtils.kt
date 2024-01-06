package org.sereinfish.catcat.image.skiko.tools.utils

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.Surface

fun Surface.size() = FloatSize(width, height)