package org.sereinfish.catcat.image.skiko.tools.draw.effect

import org.sereinfish.catcat.image.skiko.tools.draw.Draw
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo

/**
 * 形状阴影
 */
interface ShapeShadow: Shape {
    // 阴影信息
    var shadowInfo: ShadowInfo?

    /**
     * 阴影绘制方法
     */
    fun shapeShadowDraw(): Draw
}