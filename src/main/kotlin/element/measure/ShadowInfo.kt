package element.measure

import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.MaskFilter

/**
 * 阴影信息的描述
 */
data class ShadowInfo(
    val dx: Float,
    val dy: Float,
    val sigmaX: Float,
    val sigmaY: Float,
    val color: Int,
){
    constructor(dx: Number, dy: Number, radius: Number, color: Int): this(dx.toFloat(), dy.toFloat(), radius.toFloat(), radius.toFloat(), color)
    constructor(d: Number, radius: Number, color: Int): this(d.toFloat(), d.toFloat(), radius.toFloat(), radius.toFloat(), color)

    /**
     * 获取图片滤镜
     */
    fun getDropShadowImageFilter() = ImageFilter.makeDropShadow(dx, dy, sigmaX, sigmaY, color)
    fun getDropShadowImageFilterOnly() = ImageFilter.makeDropShadowOnly(dx, dy, sigmaX, sigmaY, color)
}