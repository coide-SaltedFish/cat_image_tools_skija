package org.sereinfish.catcat.image.skiko.tools.build.extend

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextFieldElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and

/**
 * 在布局中添加元素
 */
fun Layout.text(
    modifier: Modifier<in TextElement>? = null,
    text: String,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体,
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    enableAdaptiveFontSize: Boolean = false, // 自适应文本大小
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextElement.() -> Unit = {}
): TextElement {
    val textElement = TextElement(
        text,
        font = font,
        color = color,
        wordSpace = wordSpace.toFloat(),
        shadow = shadow,
        alignment = alignment,
        paintBuilder = paint,
        isTextCompact = isTextCompact,
        enableAdaptiveFontSize = enableAdaptiveFontSize
    )
    modifier?.modifier(textElement)
    textElement.block()

    add(textElement)

    return textElement
}

fun Layout.text(
    modifier: Modifier<in TextElement>? = null,
    text: String,
    typeface: Typeface = Typeface.makeDefault(),
    fontSize: Number = 18,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    enableAdaptiveFontSize: Boolean = false, // 自适应文本大小
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextElement.() -> Unit = {}
): TextElement {
    return text(
        modifier,
        text,
        color,
        wordSpace,
        Font(typeface, fontSize.toFloat()),
        shadow,
        alignment,
        isTextCompact,
        enableAdaptiveFontSize,
        paint,
        block
    )
}

fun Layout.text(
    modifier: Modifier<in TextElement>? = null,
    text: String,
    fontName: String = "黑体",
    fontStyle: FontStyle = FontStyle.NORMAL,
    fontSize: Number = 18,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    enableAdaptiveFontSize: Boolean = false, // 自适应文本大小
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextElement.() -> Unit = {}
): TextElement {
    return text(
        modifier,
        text,
        color,
        wordSpace,
        Font(Typeface.makeFromName(fontName, fontStyle), fontSize.toFloat()),
        shadow,
        alignment,
        isTextCompact,
        enableAdaptiveFontSize,
        paint,
        block
    )
}

fun Layout.textField(
    modifier: Modifier<in TextFieldElement>? = null,
    text: String,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    lineSpace: Number = 0f, // 行间距
    font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体,
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextFieldElement.() -> Unit = {}
): TextFieldElement {
    val textElement = TextFieldElement(
        text,
        font = font,
        color = color,
        wordSpace = wordSpace.toFloat(),
        lineSpace = lineSpace.toFloat(),
        shadow = shadow,
        alignment = alignment,
        paintBuilder = paint,
        isTextCompact = isTextCompact
    )
    modifier?.modifier(textElement)
    textElement.block()

    add(textElement)

    return textElement
}

fun Layout.textField(
    modifier: Modifier<in TextFieldElement>? = null,
    text: String,
    typeface: Typeface = Typeface.makeDefault(),
    fontSize: Number = 18,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    lineSpace: Number = 0f, // 行间距
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextFieldElement.() -> Unit = {}
): TextFieldElement {
    return textField(
        modifier = modifier,
        text = text,
        font = Font(typeface, fontSize.toFloat()),
        color = color,
        wordSpace = wordSpace,
        lineSpace = lineSpace,
        shadow = shadow,
        alignment = alignment,
        isTextCompact = isTextCompact,
        paint = paint,
        block = block
    )
}

fun Layout.textField(
    modifier: Modifier<in TextFieldElement>? = null,
    text: String,
    fontName: String = "黑体",
    fontStyle: FontStyle = FontStyle.NORMAL,
    fontSize: Number = 18,
    color: Int = Color.BLACK, // 字体颜色
    wordSpace: Number = 0f, // 字间距
    lineSpace: Number = 0f, // 行间距
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    isTextCompact: Boolean = false,
    paint: (Paint.() -> Unit)? = null, // 画笔构建
    block: TextFieldElement.() -> Unit = {}
): TextFieldElement {
    return textField(
        modifier = modifier,
        text = text,
        font = Font(Typeface.makeFromName(fontName, fontStyle), fontSize.toFloat()),
        color = color,
        wordSpace = wordSpace,
        lineSpace = lineSpace,
        shadow = shadow,
        alignment = alignment,
        isTextCompact = isTextCompact,
        paint = paint,
        block = block
    )
}