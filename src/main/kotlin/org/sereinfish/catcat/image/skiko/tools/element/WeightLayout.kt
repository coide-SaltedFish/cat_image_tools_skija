package org.sereinfish.catcat.image.skiko.tools.element

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.sumOf
import org.sereinfish.catcat.image.skiko.tools.utils.sumSizeOf

interface WeightLayout: Layout {
    // 元素占比总和
    var weightSum: Float
        get() = attributes.getOrElse("weightSum") {
            subElements.sumOf { it.weight }
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
    var Element.weight: Float
        get() = this.attributes.getOrElse("weight") { 0f }
        set(value) { this.attributes["weight"] = value }

    fun <T: Element> Modifier<T>.weight(value: Number) = modifier {
        this.weight = value.toFloat()
    }


    /**
     * 参数更新
     */
    override fun updateElementInfo() {
        val padding = attributes.getOrDefault("padding", FloatRectSize())

        weightSum = subElements.sumOf { it.weight }
        weightMaxSize = size().minus(padding.size()).minus(subElements.sumSizeOf {
            val w = if (it.sizeMode.contain(ElementSizeMode.MaxWidth)) 0f else it.size.width
            val h = if (it.sizeMode.contain(ElementSizeMode.MaxHeight)) 0f else it.size.height
            FloatSize(w, h)
        })
    }

    /**
     * 计算子元素所能拥有的大小
     */
    fun subElementWeightSize(element: Element): FloatSize {
        return (weightMaxSize.copy() * (element.weight / weightSum))
    }
}