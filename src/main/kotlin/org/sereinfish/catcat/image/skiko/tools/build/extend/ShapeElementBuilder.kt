package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.jetbrains.skia.Paint
import org.jetbrains.skia.Path
import org.jetbrains.skia.RRect
import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.ArcElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.RRectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.RectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.ShapeElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize

fun Layout.shape(
    modifier: Modifier<ShapeElement> = Modifier(),
    path: Path.(element: ShapeElement) -> Unit,
    antiAlias: Boolean = true,
    shadowInfo: ShadowInfo? = null,
    paint: Paint.() -> Unit = {},
    block: ShapeElement.() -> Unit = {}
): ShapeElement {
    val element = ShapeElement(path, antiAlias, shadowInfo, paint)
    modifier.modifier(element)
    element.block()

    add(element)

    return element
}

/**
 * 矩形构建
 */
fun Layout.rect(
    modifier: Modifier<RectElement> = Modifier(),
    width: Number,
    height: Number,
    color: Int,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.(element: RectElement) -> Unit = {},
    block: RectElement.() -> Unit = {}
) = RectElement(width.toFloat(), height.toFloat(), color, shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()
    add(it)
}

fun Layout.rect(
    modifier: Modifier<RectElement> = Modifier(),
    size: FloatSize,
    color: Int,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.(element: RectElement) -> Unit = {},
    block: RectElement.() -> Unit = {}
) = rect(modifier, size.width, size.height, color, shadowInfo, paintBuilder, block)

/**
 * 圆弧构建
 */
fun Layout.arc(
    modifier: Modifier<ArcElement> = Modifier(),
    oval: Rect,
    startAngle: Float,
    sweepAngle: Float,
    includeCenter: Boolean, // 决定两个端点是相互连接还是各自和圆心连接
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.() -> Unit = {},
    block: ArcElement.() -> Unit = {}
) = ArcElement(oval, startAngle, sweepAngle, includeCenter, shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()
    add(it)
}

/**
 * 圆形构建
 */
fun Layout.circular(
    modifier: Modifier<ArcElement> = Modifier(),
    radius: Float, shadowInfo: ShadowInfo? = null, paintBuilder: Paint.() -> Unit = {},
    block: ArcElement.() -> Unit = {}
) = ArcElement.circular(radius, shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()

    add(it)
}

/**
 * 圆角矩形构建
 */
fun Layout.rRect(
    modifier: Modifier<RRectElement> = Modifier(),
    rRect: RRect,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.() -> Unit = {},
    block: RRectElement.() -> Unit = {}
) = RRectElement(rRect, shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()

    add(it)
}

fun Layout.rRect(
    modifier: Modifier<RRectElement> = Modifier(),
    width: Number, height: Number, radius: Number,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.() -> Unit = {},
    block: RRectElement.() -> Unit = {}
) = RRectElement(width.toFloat(), height.toFloat(), radius.toFloat(), shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()

    add(it)
}

fun Layout.rRect(
    modifier: Modifier<RRectElement> = Modifier(),
    width: Number, height: Number, xRad: Number, yRad: Number,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.() -> Unit = {},
    block: RRectElement.() -> Unit = {}
) = RRectElement(width.toFloat(), height.toFloat(), xRad.toFloat(), yRad.toFloat(), shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()

    add(it)
}

fun Layout.rRect(
    modifier: Modifier<RRectElement> = Modifier(),
    width: Number, height: Number, tlRad: Number, trRad: Number, brRad: Number, blRad: Number,
    shadowInfo: ShadowInfo? = null,
    paintBuilder: Paint.() -> Unit = {},
    block: RRectElement.() -> Unit = {}
) = RRectElement(width.toFloat(), height.toFloat(), tlRad.toFloat(), trRad.toFloat(), brRad.toFloat(), blRad.toFloat(), shadowInfo, paintBuilder).also {
    modifier.modifier(it)
    it.block()

    add(it)
}