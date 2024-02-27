package org.sereinfish.catcat.image.skiko.tools.element

import mu.KotlinLogging
import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementAttrContext
import org.sereinfish.catcat.image.skiko.tools.element.draw.ElementDrawChain
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.offset.FloatOffset
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import java.util.concurrent.ConcurrentHashMap

/**
 * 元素大小计算
 * 1. width = widthUpdate()
 *    height = heightUpdate()
 */
abstract class AbstractElement(
    override var parent: Layout? = null, // 父元素
    override var beforeDrawChain: ElementDrawChain = ElementDrawChain(), // 前置绘制器
    override var elementDraw: Draw = buildDraw { _ -> }, // 元素绘制器
    override var afterDrawChain: ElementDrawChain = ElementDrawChain(), // 后置绘制器
): Element {
    protected val logger = KotlinLogging.logger("AbstractElement")
    final override var attributes: ElementAttrContext = ElementAttrContext()

    protected var initHeight: Boolean by attributes.valueOrElse { false }
    protected var initWidth: Boolean by attributes.valueOrElse { false }

    override var size: FloatSize by attributes.valueOrElse { FloatSize() } // 这个大小来自缓存

    override var sizeMode: ElementSizeMode by attributes.valueOrElse { ElementSizeMode.Auto }

    var padding by attributes.valueOrDefault(FloatRectSize())

    // 大小计算扩展模块
    var sizeExtendModules = ConcurrentHashMap<String, (FloatSize) -> Unit>()

    /**
     * 自动模式下的元素大小
     */
    open fun autoSize(): FloatSize {
        return FloatSize(width(), height()).add(padding.size())
    }

    fun initSizeFlag(){
        logger.debug { "初始化元素更新标志: ${this}" }

        initHeight = false
        initWidth = false
    }

    override fun updateWidth() {
        if (initWidth)  {
            logger.debug("元素宽度更新中止，因为元素宽度已计算")
            return
        }
        initWidth = true

        width = when {
            sizeMode.contain(ElementSizeMode.AutoWidth) -> width()
            sizeMode.contain(ElementSizeMode.ValueWidth) -> width
            sizeMode.contain(ElementSizeMode.MaxWidth) -> parent?.subElementMaxWidth(this) ?: 0f
            else -> {
                logger.warn { "尚未设置任意 WidthSizeMode，使用默认值 AutoWidth" }
                width()
            }
        }
    }

    override fun updateHeight() {
        if (initHeight) {
            logger.debug { "元素高度更新中止，因为元素高度已计算" }
            return
        }
        initHeight = true

        height = when {
            sizeMode.contain(ElementSizeMode.AutoHeight) -> height()
            sizeMode.contain(ElementSizeMode.ValueHeight) -> height
            sizeMode.contain(ElementSizeMode.MaxHeight) -> parent?.subElementMaxHeight(this) ?: 0f
            else -> {
                logger.warn { "尚未设置任意 HeightSizeMode，使用默认值 AutoHeight" }
                height()
            }
        }
    }

    /**
     * 获取元素的大小
     *
     * 这个大小是实时计算的
     */
    override fun size(): FloatSize {
        updateWidth()
        updateHeight()

        // 使用扩展计算大小
        sizeExtendModules.values.forEach { it(size) }

        return size
    }

    /**
     * 计算元素属性
     *
     * 计算元素绘制坐标
     */
    override fun updateElementInfo() {
        val parentPadding: FloatRectSize = parent?.attributes?.get("padding") as? FloatRectSize ?: FloatRectSize()
        // 计算相对坐标
        attributes.offset = parent?.subElementOffset(this)?.copy() ?: FloatOffset()

        // 计算绝对坐标
        attributes.offsetAbsolute = parent?.attributes?.offsetAbsolute?.copy()?.add(attributes.offset) ?: attributes.offset.copy()
        attributes.offsetAbsolute.add(parentPadding.offset())
    }
}
