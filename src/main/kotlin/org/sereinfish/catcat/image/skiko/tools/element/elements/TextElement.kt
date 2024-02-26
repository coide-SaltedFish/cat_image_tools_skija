package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.AlignmentLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import org.sereinfish.catcat.image.skiko.tools.utils.saveBlock
import kotlin.math.roundToInt

/**
 * 基本的文本元素
 *
 * 只能绘制一行
 */
open class TextElement(
    var text: String, // 字符串
    var font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体
    var wordSpace: Float = 0f, // 字间距

    var color: Int = Color.BLACK, // 字体颜色
    var shadow: ShadowInfo? = null, // 文字阴影
    override var alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    var paintBuilder: (Paint.() -> Unit)? = null, // 画笔构建
    var isTextCompact: Boolean = false, // 紧凑绘制文本
    var enableAdaptiveFontSize: Boolean = false, // 自适应文本大小
) : AbstractElement(), AlignmentLayout {

    protected val paint: Paint get() = buildPaint() // 获取实时构建的 Paint

    /**
     * 将字符大小存入缓存
     * 该缓存在一次绘制中只应该更新一次
     */
    protected var charsRect: Map<Char, Rect> by attributes.valueOrElse {
        buildMap {
            text.toSet().forEach {
                put(it, font.measureText("$it", paint))
            }

            put('■', font.measureText("■", paint))
            put('▌', font.measureText("▌", paint))
        }
    }

    init {
        // 定义元素绘制器
        elementDraw = buildDraw { context ->
            if (text.isNotEmpty()){
                saveBlock({
                    translate(padding.left, padding.top)
                    clipRect(Rect.makeWH(size.width, size.height))
                }) {
                    val offset = getTextDrawOffset() // 获取起始坐标

                    text.forEachIndexed { index, c ->
                        if (index > 0) offset.x += getIndexCharWidth(index - 1) // 计算上一个字符的宽度
                        drawString("$c", offset.x, offset.y, font, paint) // 绘制字符
                    }

                    offset.x += getIndexCharWidth(text.length - 1)
                }
            }
        }
    }

    /**
     * 获取文本绘制坐标
     */
    private fun getTextDrawOffset(): FloatOffset {
        val rect = getStringDrawRect(text)

        val offset = alignment(size.copy().minus(padding.size()), getTextDrawSize()).apply {
            if (isTextCompact)
                x -= rect.left
            y -= rect.top
        }

        return offset
    }

    /**
     * 获取文本绘制出来的大小
     */
    private fun getTextDrawSize(): FloatSize {
        return getTextDrawSize(text)
    }

    protected fun getTextDrawSize(str: String): FloatSize {
        val rect = getStringDrawRect(str)
        var width = 0f
        for (i in str.indices){
            width += getIndexCharWidth(i)
        }
        width += (text.length - 1) * wordSpace
        return FloatSize(width, rect.height)
    }

    /**
     * 获取指定位置字符大小
     */
    private fun getIndexCharWidth(i: Int): Float {
        val rect = getCharDrawRect(text[i])
        var width = rect.width

        if (i != 0)
            width += wordSpace
        if (isTextCompact.not())
            width += rect.left

        return width
    }

    protected fun getStringDrawRect(text: String): Rect {
        var l = 0f
        var r = 0f
        var t = 0f
        var b = 0f
        text.map { getCharDrawRect(it) }.forEach {
            l = minOf(l, it.left)
            r += it.right
            t = minOf(t, it.top)
            b = maxOf(b, it.bottom)
        }

        return Rect.makeLTRB(l, t, r, b)
    }

    /**
     * 获取单个字符大小
     */
    protected fun getCharDrawRect(c: Char): Rect {
        if ("，。？＇！……".contains(c)) return charsRect['■'] ?: font.measureText("■", paint)
        if (",.?\\'! ……".contains(c)) return charsRect['▌'] ?: font.measureText("▌", paint)
        return charsRect[c] ?: font.measureText("$c", paint)
    }

    /**
     * 获取元素大小
     */
    private fun getElementSize(): FloatSize {
        return getTextDrawSize().add(padding.size())
    }

    /**
     * 自适应文本大小
     */
    protected fun adaptiveFontSize(){
        if (enableAdaptiveFontSize){
            val width = size.width - padding.size().width

            if (sizeMode.contain(ElementSizeMode.AutoHeight).not() and sizeMode.contain(ElementSizeMode.AutoWidth).not()){
                // 求个大概的值
                font.size = ((width - text.length * wordSpace) / text.length).roundToInt().toFloat()
                // 循环匹配
                var w = getTextDrawSize(text).width
                while (w < width){
                    font.size ++
                    w = getTextDrawSize(text).width
                }
                font.size --

                font.size = minOf(
                    size.height - padding.size().height,
                    font.size
                )
            }else {
                if (sizeMode.contain(ElementSizeMode.AutoHeight).not()){
                    font.size = size.height - padding.size().height
                }

                if (sizeMode.contain(ElementSizeMode.AutoWidth).not() and enableAdaptiveFontSize){
                    // 求个大概的值
                    font.size = ((width - text.length * wordSpace) / text.length).roundToInt().toFloat()
                    // 循环匹配
                    var w = getTextDrawSize(text).width
                    while (w < width){
                        font.size ++
                        w = getTextDrawSize(text).width
                    }
                    font.size --
                }
            }
        }
    }

    /**
     * 重写自动大小
     */
    override fun autoSize(): FloatSize {
        return if (text.isEmpty()) FloatSize() else {
            adaptiveFontSize()
            getElementSize()
        }
    }

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