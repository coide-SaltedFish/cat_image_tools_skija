package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.sumOf
import org.sereinfish.catcat.image.skiko.tools.utils.sumSizeOf

interface WeightLayout: Layout {
    // 元素占比总和
    var weightSum: FloatSize
        get() = attributes.getOrElse("weightSum") {
            subElements.sumSizeOf { it.weight }
        }
        set(value) = attributes.set("weightSum", value)

    // 子元素能拥有的最大可分配空间
    var weightMaxSize: FloatSize
        get() = attributes.getOrElse("weightMaxSize") {
            FloatSize()
        }
        set(value) = attributes.set("weightMaxSize", value)

    /**
     * 子元素的扩展元素
     * 仅在子元素设置为最大大小时有效
     * 该属性返回的值由布局提供
     */
    var Element.weight: FloatSize
        get() = this.attributes.getOrElse("weight") { FloatSize() }
        set(value) { this.attributes["weight"] = value }

    /**
     * Modifier 扩展方法，设置元素的权重大小
     */
    fun <T: Element> Modifier<T>.weight(value: Number) = modifier {
        this.weight = FloatSize(value.toFloat())
    }

    fun <T: Element> Modifier<T>.weight(width: Number = 0, height: Number = 0) = modifier {
        this.weight = FloatSize(width.toFloat(), height.toFloat())
    }

    /**
     * 作为比例布局，计算子元素大小
     */
    override fun updateSize() {
        val padding = attributes.getOrDefault("padding", FloatRectSize())

        weightSum = subElements.sumSizeOf { it.weight }
        weightMaxSize = size.copy().minus(padding.size()).minus(subElements.sumSizeOf {
            val w = if (it.sizeMode.contain(ElementSizeMode.MaxWidth)) 0f else it.size.width
            val h = if (it.sizeMode.contain(ElementSizeMode.MaxHeight)) 0f else it.size.height
            FloatSize(w, h)
        })
    }

    /**
     * 计算子元素所能拥有的大小
     */
    fun subElementWeightSize(element: Element): FloatSize {
        return FloatSize(
            weightMaxSize.width * (element.weight.width / weightSum.width),
            weightMaxSize.height * (element.weight.height / weightSum.height)
        )
    }
}