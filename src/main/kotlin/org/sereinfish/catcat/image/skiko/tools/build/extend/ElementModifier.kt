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
import org.sereinfish.catcat.image.skiko.tools.A
import org.sereinfish.catcat.image.skiko.tools.draw.effect.arcShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.circularShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.rRectShape
import org.sereinfish.catcat.image.skiko.tools.draw.effect.shape
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset

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
    paintBuilder: Paint.() -> Unit = {}, // 构建 Paint
) = modifier {
    beforeDrawChain.plus(ElementImageDraw(size, image, samplingMode, cropMode, Alignment.CENTER, paintBuilder))
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
