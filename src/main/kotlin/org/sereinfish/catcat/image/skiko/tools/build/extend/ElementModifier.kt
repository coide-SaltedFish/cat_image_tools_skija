package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.draw.draws.ElementColorDraw
import org.sereinfish.catcat.image.skiko.tools.draw.draws.ElementImageDraw
import org.sereinfish.catcat.image.skiko.tools.draw.draws.ElementStrokeDraw
import org.sereinfish.catcat.image.skiko.tools.draw.utils.ElementUtils
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.draw.effect.arcShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.circularShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.rRectShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.shape
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

/**
 * 元素背景颜色
 */
fun <T: Element> Modifier<T>.background(color: Int) = modifier {
    beforeDrawChain.plus(ElementColorDraw(this, color))
}

/**
 * 元素背景图片
 */
fun <T: Element> Modifier<T>.background(
    image: Image,
    samplingMode: SamplingMode = SamplingMode.DEFAULT, // 图片采样模式
    cropMode: CropMode = CropMode.Fit,
    alignment: Alignment = Alignment.CENTER,
    paintBuilder: Paint.() -> Unit = {}, // 构建 Paint
) = modifier {
    beforeDrawChain.plus(ElementImageDraw(this, image, samplingMode, cropMode, alignment, paintBuilder))
}

/**
 * 元素背景绘制
 */
fun <T: AbstractElement> Modifier<T>.background(block: Canvas.(element: AbstractElement) -> Unit) = modifier {
    beforeDrawChain.plus(buildDraw {
        block(this@modifier)
    })
}

/**
 * 元素大小模式设置
 */
fun <T: Element> Modifier<T>.sizeMode(mode: ElementSizeMode) = modifier {
    sizeMode = mode
}

/**
 * 元素大小设置
 */
fun <T: Element> Modifier<T>.width(width: Number) = modifier {
    sizeMode = sizeMode.and(ElementSizeMode.ValueWidth)
    size.width = width.toFloat()
}

fun <T: Element> Modifier<T>.height(height: Number) = modifier {
    sizeMode = sizeMode.and(ElementSizeMode.ValueHeight)
    size.height = height.toFloat()
}

fun <T: Element> Modifier<T>.size(width: Number, height: Number) = modifier {
    sizeMode = ElementSizeMode.Value
    size.width = width.toFloat()
    size.height = height.toFloat()
}

fun <T: Element> Modifier<T>.size(value: Number) = size(value, value)
fun <T: Element> Modifier<T>.size(value: FloatSize) = modifier {
    sizeMode = ElementSizeMode.Value
    size = value
}

fun <T: Element> Modifier<T>.maxSize() = modifier {
    sizeMode = ElementSizeMode.MaxFill
}

fun <T: Element> Modifier<T>.maxWidth() = modifier {
    sizeMode = sizeMode.and(ElementSizeMode.MaxWidth)
}

fun <T: Element> Modifier<T>.maxHeight() = modifier {
    sizeMode = sizeMode.and(ElementSizeMode.MaxHeight)
}

/**
 * 内边距
 */
fun <T: AbstractElement> Modifier<T>.padding(value: Number) = modifier {
    padding = FloatRectSize(value)
}

fun <T: AbstractElement> Modifier<T>.padding(
    left: Number = 0f,
    top: Number = 0f,
    bottom: Number = 0f,
    right: Number = 0f,
) = modifier {
    padding = FloatRectSize(left.toFloat(), right.toFloat(), top.toFloat(), bottom.toFloat())
}

fun <T: AbstractElement> Modifier<T>.padding(
    lr: Number = 0f,
    tb: Number = 0f,
) = modifier {
    padding = FloatRectSize(lr.toFloat(), lr.toFloat(), tb.toFloat(), tb.toFloat())
}

/**
 * 元素边框
 */
fun <T: Element> Modifier<T>.border(
    strokeWidth: Number = 1,
    color: Int = Color.BLACK
) = modifier {
    afterDrawChain.plus(ElementStrokeDraw(this, strokeWidth.toFloat(), color))
}


/**
 * 高斯模糊背景
 */
fun <T: AbstractElement> Modifier<T>.blurBackground(
    sigmaX: Number,
    sigmaY: Number,
    clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
    offset: FloatOffset = FloatOffset(),
    isFirst: Boolean = false
) = modifier {
    ElementUtils.blurBackground(this, sigmaX.toFloat(), sigmaY.toFloat(), clipPath, offset, isFirst)
}

fun <T: AbstractElement> Modifier<T>.blurBackground(
    radius: Number,
    clipPath: (Path.(element: AbstractElement) -> Unit)? = null,
    offset: FloatOffset = FloatOffset(),
    isFirst: Boolean = false
) = blurBackground(radius, radius, clipPath, offset, isFirst)

/**
 * 高斯模糊
 */
fun <T: Element> Modifier<T>.blur(
    sigmaX: Number,
    sigmaY: Number,
) = modifier {
    ElementUtils.blur(this, sigmaX.toFloat(), sigmaY.toFloat())
}

fun <T: Element> Modifier<T>.blur(
    radius: Number,
) = modifier {
    ElementUtils.blur(this, radius.toFloat(), radius.toFloat())
}

/**
 * 限制元素的绘制形状
 */
fun <T: AbstractElement> Modifier<T>.shape(
    path: AbstractElement.() -> Path,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.shape(path, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

fun <T: AbstractElement> Modifier<T>.rRectShape(
    tlRad: Number, trRad: Number, brRad: Number, blRad: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.rRectShape(tlRad, trRad, brRad, blRad, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

fun <T: AbstractElement> Modifier<T>.rRectShape(
    xRad: Number, yRad: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.rRectShape(xRad, yRad, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

fun <T: AbstractElement> Modifier<T>.rRectShape(
    radius: Number,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.rRectShape(radius, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

fun <T: AbstractElement> Modifier<T>.arcShape(
    startAngle: Float, sweepAngle: Float, includeCenter: Boolean,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.arcShape(startAngle, sweepAngle, includeCenter, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

fun <T: AbstractElement> Modifier<T>.circularShape(
    includeCenter: Boolean = true,
    stroke: Boolean = false, strokeColor: Int = Color.BLACK, strokeWidth: Number = 1,
    padding: FloatRectSize = FloatRectSize(),
    shadowInfo: ShadowInfo? = null,
    mode: ClipMode = ClipMode.INTERSECT, antiAlias: Boolean = true
) = modifier {
    this.circularShape(includeCenter, stroke, strokeColor, strokeWidth, padding, shadowInfo, mode, antiAlias)
}

// 最小大小
var AbstractElement.minSize: FloatSize?
    get() = attributes.getOrElse("minSize"){ null }
    set(value) {
        attributes["minSize"] = value
    }

// 最大大小
var AbstractElement.maxSize: FloatSize?
    get() = attributes.getOrElse("maxSize"){ null }
    set(value) {
        attributes["maxSize"] = value
    }

fun <T: AbstractElement> Modifier<T>.minSize(w: Number, h: Number) = modifier {
    minSize = FloatSize(w.toFloat(), h.toFloat())
    sizeExtendModules["minSize"] = { size ->
        minSize?.let {
            if (it.width != 0f) size.width = maxOf(it.width, size.width)
            if (it.height != 0f) size.height = maxOf(it.height, size.height)
        }
    }
}

fun <T: AbstractElement> Modifier<T>.minWidth(w: Number) = modifier {
    minSize?.let {
        it.width = w.toFloat()
    } ?: run {
        minSize = FloatSize(w.toFloat(), 0f)
    }
    sizeExtendModules["minSize"] = { size ->
        minSize?.let {
            if (it.width != 0f) size.width = maxOf(it.width, size.width)
            if (it.height != 0f) size.height = maxOf(it.height, size.height)
        }
    }
}

fun <T: AbstractElement> Modifier<T>.minHeight(h: Number) = modifier {
    minSize?.let {
        it.height = h.toFloat()
    } ?: run {
        minSize = FloatSize(0f, h.toFloat())
    }
    sizeExtendModules["minSize"] = { size ->
        minSize?.let {
            if (it.width != 0f) size.width = maxOf(it.width, size.width)
            if (it.height != 0f) size.height = maxOf(it.height, size.height)
        }
    }
}

fun <T: AbstractElement> Modifier<T>.maxSize(w: Number, h: Number) = modifier {
    maxSize = FloatSize(w.toFloat(), h.toFloat())
    sizeExtendModules["maxSize"] = { size ->
        // 进行最大最小大小计算
        this.maxSize?.let {
            if (it.width != 0f) size.width = minOf(it.width, size.width)
            if (it.height != 0f) size.height = minOf(it.height, size.height)
        }
    }
}

fun <T: AbstractElement> Modifier<T>.maxWidth(w: Number) = modifier {
    maxSize?.let {
        it.width = w.toFloat()
    } ?: run {
        maxSize = FloatSize(w.toFloat(), 0f)
    }
    sizeExtendModules["maxSize"] = { size ->
        // 进行最大最小大小计算
        this.maxSize?.let {
            if (it.width != 0f) size.width = minOf(it.width, size.width)
            if (it.height != 0f) size.height = minOf(it.height, size.height)
        }
    }

}

fun <T: AbstractElement> Modifier<T>.maxHeight(h: Number) = modifier {
    maxSize?.let {
        it.height = h.toFloat()
    } ?: run {
        maxSize = FloatSize(0f, h.toFloat())
    }
    sizeExtendModules["maxSize"] = { size ->
        // 进行最大最小大小计算
        this.maxSize?.let {
            if (it.width != 0f) size.width = minOf(it.width, size.width)
            if (it.height != 0f) size.height = minOf(it.height, size.height)
        }
    }
}

// 正方形模式
var AbstractElement.squareMode: Boolean
    get() = attributes.getOrElse("squareMode"){ false }
    set(value) {
        attributes["squareMode"] = value
    }

fun <T: AbstractElement> Modifier<T>.square() = squareMode(true)

fun <T: AbstractElement> Modifier<T>.squareMode(mode: Boolean = true) = modifier {
    squareMode = mode

    sizeExtendModules["square"] = { size ->
        // 正方形设置
        if (squareMode){
            val v = maxOf(size.width, size.height)
            size.width = v
            size.height = v
        }
    }
}

// 比例内边距
var AbstractElement.scalePadding: FloatRectSize?
    get() = attributes.getOrElse("scalePadding"){ null }
    set(value) {
        attributes["scalePadding"] = value
    }

fun <T: AbstractElement> Modifier<T>.scalePadding(
    l: Number = 0,
    r: Number = 0,
    t: Number = 0,
    b: Number = 0
) = modifier {
    sizeExtendModules["scalePadding"] = { size ->
        padding = FloatRectSize(
            left = l.toFloat() * size.width,
            right = r.toFloat() * size.width,
            top = t.toFloat() * size.height,
            bottom = b.toFloat() * size.height,
        )
    }
}

fun <T: AbstractElement> Modifier<T>.scalePadding(value: Number) = scalePadding(value, value, value, value)

fun <T: AbstractElement> Modifier<T>.scalePadding(lr: Number = 0, tb: Number = 0) = scalePadding(lr, lr, tb, tb)
