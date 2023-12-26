package draw.effect

import draw.Draw
import element.measure.ShadowInfo

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