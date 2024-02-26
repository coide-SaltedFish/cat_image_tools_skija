package org.sereinfish.catcat.image.skiko.tools.element.elements

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.extend.row
import org.sereinfish.catcat.image.skiko.tools.build.extend.size
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.forEachWithSeparator
import org.sereinfish.catcat.image.skiko.tools.utils.paint
import java.util.Vector

/**
 * 文本域
 *
 * TODO 取消使用ColumLayout容器
 */
class TextFieldElement(
    val text: String, // 字符串
    var subModifier: Modifier<in TextElement> = Modifier(),
    var font: Font = Font(Typeface.makeFromName("黑体", FontStyle.NORMAL), 18f), // 字体
    var wordSpace: Float = 0f, // 字间距
    var lineSpace: Float = 0f, // 行间距

    var color: Int = Color.BLACK, // 字体颜色
    var shadow: ShadowInfo? = null, // 文字阴影
    alignment: Alignment = Alignment.LEFT.and(Alignment.CENTER_VERTICAL), // 对齐方式
    var paintBuilder: (Paint.() -> Unit)? = null, // 画笔构建
    var isTextCompact: Boolean = false // 紧凑绘制文本
): ColumLayout(alignment) {
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

    protected val paint: Paint get() = buildPaint() // 获取实时构建的 Paint

    // 完成文本行分割，然后依次添加到布局
    fun updateTextField(){
        if (subElements.isNotEmpty()) subElements.clear()
        stringLines().forEachWithSeparator({
            if (lineSpace > 0)
                row(Modifier<RowLayout>().size(0.1, lineSpace))
        }) {
            add(TextElement(it, font, wordSpace, color, shadow, paintBuilder = paintBuilder, isTextCompact = isTextCompact).apply {
                subModifier.modifier(this)
            })
        }
    }

    override fun updateSize() {
        size = size()
        updateTextField()
        super.updateSize()
    }

    /**
     * 根据文本换行符以及元素大小完成文本分割
     */
    protected fun stringLines(): List<String> {
        val strs = text.split("\n")

        // 如果宽度为自动
        return if((sizeMode.contain(ElementSizeMode.MaxWidth) or sizeMode.contain(ElementSizeMode.ValueWidth)).not()){
            strs
        }else {
            buildList {
                // 需要获取元素的具体宽度
                val w = if (sizeMode.contain(ElementSizeMode.MaxWidth))
                    maxSize().minus(padding.size()).width
                else this@TextFieldElement.size.minus(padding.size()).width

                // 开始计算字符能否放下
                strs.forEach {
                    if (getTextDrawSize(it).width < w)
                        add(it)
                    else {
                        var offset = 0
                        for (i in it.indices){
                            if (getTextDrawSize(it.substring(offset, i + 1)).width > w){
                                add(it.substring(offset, i))
                                offset = i
                            }
                        }
                        if (offset != it.length)
                            add(it.substring(offset, it.length))
                    }
                }
            }
        }
    }

    /**
     * 重写自动大小
     *
     * 需要考虑的情况
     * 一、文本中有换行
     * 二、布局限制需要换行
     *
     * 1. 自动大小  检查换行符，一行一行的绘制
     * 2. 限制宽度 根据宽度和换行符计算行
     * 3. 最大大小 根据布局返回的大小计算行
     *
     * 处理顺序
     * 1. 根据换行符进行换行处理
     * 2. 计算元素所能拥有的空间，如果为自动则为无限大
     * 3. 根据空间大小再次进行换行计算处理
     * 4. 根据行数计算元素的最终大小
     * 5. 根据分配好的文本行数完成文本绘制
     */
//    override fun autoSize(): FloatSize {
//        val lines = stringLines()
//        val size = getTextDrawSize(lines.maxBy { it.length })
//        val height = lines.sumOf { getTextDrawSize(it).height } + (lines.size - 1) * lineSpace
//        return FloatSize(size.width, height).add(padding.size())
//    }

    protected fun getTextDrawSize(str: String): FloatSize {
        val rect = getStringDrawRect(str)
        var width = 0f
        for (i in str.indices){
            val cr = getCharDrawRect(str[i])
            width += cr.width + wordSpace + cr.left
        }
        width += (text.length - 1) * wordSpace
        return FloatSize(width, rect.height)
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
     * 获取元素的 Paint
     */
    private fun buildPaint(): Paint = paint {
        color = this@TextFieldElement.color
        isAntiAlias = true
        shadow?.let {
            imageFilter = it.getDropShadowImageFilter()
        }

        paintBuilder?.invoke(this) // 构建器
    }
}