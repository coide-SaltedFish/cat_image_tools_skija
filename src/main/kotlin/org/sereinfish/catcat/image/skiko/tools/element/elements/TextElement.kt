package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock

/**
 * 基本的文本元素
 *
 * 只能绘制一行
 */
class TextElement(
    var text: String, // 字符串
    var font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体
    var wordSpace: Float = 0f, // 字间距

    var color: Int = Color.BLACK, // 字体颜色
    var shadow: ShadowInfo? = null, // 文字阴影
    override var alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    var paintBuilder: (Paint.() -> Unit)? = null // 画笔构建

) : AbstractElement(), AlignmentLayout {

    private val paint: Paint get() = buildPaint() // 获取实时构建的 Paint

    init {
        // 定义元素绘制器
        elementDraw = buildDraw { context ->
            saveBlock({
                translate(padding.left, padding.top)
                clipRect(Rect.makeWH(size.width, size.height))
            }) {
                val offset = getTextDrawOffset() // 获取起始坐标

                text.forEachIndexed { index, c ->
                    if (index > 0) offset.x += getIndexCharSize(index - 1) // 计算上一个字符的宽度
                    drawString("$c", offset.x, offset.y, font, paint) // 绘制字符
                }

                offset.x += getIndexCharSize(text.length - 1)
            }
        }
    }

    /**
     * 获取文本绘制坐标
     */
    private fun getTextDrawOffset(): FloatOffset {
        val rect = font.measureText(text, paint)

        val offset = alignment(size.copy().minus(padding.size()), getTextDrawSize())

        return FloatOffset(- rect.left, - rect.top).add(offset)
    }

    /**
     * 获取文本绘制出来的大小
     */
    private fun getTextDrawSize(): FloatSize {
        val rect = font.measureText(text, paint)
        var width = 0f
        for (i in text.indices){
            width += getIndexCharSize(i)
        }
        return FloatSize(width, rect.height)
    }

    /**
     * 获取指定位置字符大小
     */
    private fun getIndexCharSize(i: Int): Float {
        val rect = getWordDrawRectSize(text[i])
        var w = rect.width
        if (i > 0){
            w += wordSpace + rect.left
        }

        return w
    }

    /**
     * 获取单个字符大小
     */
    private fun getWordDrawRectSize(c: Char): Rect {
        if (c == ' ') return font.measureText("n", paint)

        return font.measureText("$c", paint)
    }

    /**
     * 获取元素大小
     */
    private fun getElementSize(): FloatSize {
        return getTextDrawSize().add(padding.size())
    }

    /**
     * 重写自动大小
     */
    override fun autoSize(): FloatSize = getElementSize()

    /**
     * 获取元素的 Paint
     */
    private fun buildPaint(): Paint = paint {
        color = this@TextElement.color
        isAntiAlias = true
        shadow?.let {
            imageFilter = it.getDropShadowImageFilter()
        }

        paintBuilder?.invoke(this) // 构建器
    }
}