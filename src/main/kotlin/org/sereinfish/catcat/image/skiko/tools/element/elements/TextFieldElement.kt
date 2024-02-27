package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.forEachWithSeparator
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

/**
 * 绘制流程
 * 1. 计算容器大小
 * 2. 分割文本
 * 3. 绘制文本
 */
class TextFieldElement(
    text: String, // 字符串
    font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体
    wordSpace: Float = 0f, // 字间距
    var lineSpace: Float = 0f, // 行间距
    color: Int = Color.BLACK, // 字体颜色
    shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    paintBuilder: (Paint.() -> Unit)? = null, // 画笔构建
    isTextCompact: Boolean = false, // 紧凑绘制文本
): TextElement(text, font, wordSpace, color, shadow, alignment, paintBuilder, isTextCompact) {

    // 字符位置的缓存，一次绘制仅更新一次
    protected var charsOffset: List<FloatOffset> by attributes.valueOrElse { charsOffset() }

    /**
     * 绘制文本
     */
    init {
        elementDraw = buildDraw { context ->
            if (text.isNotEmpty()){
                saveBlock({
                    translate(padding.left, padding.top)
                    clipRect(Rect.makeWH(size.width, size.height))
                }) {
                    val startOffset = getTextDrawOffset() // 获取起始坐标
                    charsOffset.forEachIndexed { index, floatOffset ->
                        val offset = floatOffset.add(startOffset)
                        drawString("${text[index]}", offset.x, offset.y, font, paint)
                    }
                }
            }
        }
    }

    /**
     * 宽度决定文本高度
     */
    override fun width(): Float {
        val lines = text.split("\n")
        return lines.maxOf { getStringWidth(it) }
    }

    /**
     * 基于宽度获取高度
     */
    override fun height(): Float {
        // 更新字符位置缓存
        charsOffset = charsOffset()

        val w = width - padding.width
        // 如果文本域为自动宽度，仅根据换行符返回高度
        if (sizeMode.contain(ElementSizeMode.AutoWidth)){
            val lines = text.split("\n")
            return lines.size * font.metrics.height
        }else {
            return charsOffset.maxOf { it.x + font.metrics.height }
        }
    }

    /**
     * 计算字符位置，此方法需要在 width 计算之后调用
     * 1. 如果文本域为自动宽度，文本宽度直接增加，高度根据换行符增加
     * 2. 其他根据文本域宽度依次计算
     */
    protected fun charsOffset(): List<FloatOffset> {
        if (width < charsRect.values.maxOf { it.width })
            logger.warn { "文本域宽度小于所容纳的字符绘制宽度: $width" }
        return buildList {
            var x = 0f
            var y = 0f
            val w = width - padding.width
            if (sizeMode.contain(ElementSizeMode.AutoWidth)){
                text.split("\n").forEachWithSeparator({
                    y += lineSpace + font.metrics.height
                    x = 0f
                    add(FloatOffset(x, y))
                }) { line ->
                    line.map { getCharDrawRect(it) }.forEachWithSeparator({
                        x += wordSpace
                        if (isTextCompact.not())
                            x += it.left
                    }) {
                        add(FloatOffset(x, y))
                        x += it.width
                    }
                }
            }else {
                for (i in text.indices){
                    val c = text[i]
                    // 换行符换行
                    if (c == '\n'){
                        y += lineSpace + font.metrics.height
                        x = 0f
                        add(FloatOffset(x, y))
                        continue
                    }
                    // 判断是否碰到边界
                    val rect = getCharDrawRect(c)
                    var cw = rect.width
                    if (isTextCompact.not()) cw += rect.left

                    if (x + cw > w){ // 换行
                        y += lineSpace + font.metrics.height
                        x = 0f
                    }else {
                        if (i <= text.length - 1) cw += wordSpace
                    }
                    add(FloatOffset(x, y))
                    x += cw
                }
            }
        }
    }

    /**
     * 获取字符串的绘制宽度
     */
    private fun getStringWidth(text: String): Float {
        var width = 0f
        for (i in text.indices){
            val rect = getCharDrawRect(text[i])
            width += rect.width

            if (i != 0)
                width += wordSpace
            if (isTextCompact.not())
                width += rect.left
        }
        return width
    }

    /**
     * 获取文本域起始坐标
     *
     * 在绘制时使用
     */
    override fun getTextDrawOffset(): FloatOffset {
        return FloatOffset().apply {
            alignment(size.copy().minus(padding.size()), textDrawSize())
        }
    }

    protected fun textDrawSize(): FloatSize {
        return FloatSize().apply {
            charsOffset.forEachIndexed { i, charOffset ->
                height = maxOf(charOffset.y + font.metrics.height, height)
                width = this@TextFieldElement.width - padding.width
            }
        }
    }
}