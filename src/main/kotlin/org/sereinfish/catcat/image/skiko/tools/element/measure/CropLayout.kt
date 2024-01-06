package org.sereinfish.catcat.image.skiko.tools.element.measure

import org.jetbrains.skia.Rect
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode.*
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import kotlin.math.max

/**
 * 使用裁剪参数的布局
 *
 */
interface CropLayout {
    var cropMode: CropMode

    /**
     * 输入容器和元素大小
     * 输入图片大小、目标大小，输出需要在图片上裁剪的区域 Rect
     */
    fun crop(src: FloatSize, dst: FloatSize): Rect {
        // 计算宽高的缩放比例
        val widthScale = src.width / dst.width
        val heightScale = src.height / dst.height

        when(cropMode){
            Fit -> { // 适应长边
                val scale = maxOf(widthScale, heightScale)

                return Rect.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }

            FitFill -> {
                val scale = minOf(widthScale, heightScale)

                return Rect.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }

            Crop -> {
                // 如果图片整体小于容器，则居中返回
                if (dst.width <= src.width && dst.height <= src.height){
                    return Rect.makeXYWH(
                        (src.width - dst.width) / 2,
                        (src.height - dst.height) / 2,
                        dst.width, dst.height
                    )
                }
                // 找到缩放比例高的一边
                val scale = maxOf(widthScale, heightScale)
                // 计算要选取的图片区域
                return Rect.makeXYWH(
                    (dst.width - src.width * scale) / 2,
                    (dst.height - src.height * scale) / 2,
                    src.width, src.height
                )
            }

            FillHeight -> {
                return Rect.makeXYWH(
                    (src.width - dst.width * heightScale) / 2,
                    (src.height - dst.height * heightScale) / 2,
                    dst.width * heightScale, dst.height * heightScale
                )
            }
            FillWidth -> {
                return Rect.makeXYWH(
                    (src.width - dst.width * widthScale) / 2,
                    (src.height - dst.height * widthScale) / 2,
                    dst.width * widthScale, dst.height * widthScale
                )
            }
            FillBounds -> {
                return Rect.makeXYWH(
                    0f, 0f, dst.width * widthScale, dst.height * heightScale
                )
            }
            Inside -> {
                // 如果图片整体小于容器，则居中返回
                if (dst.width <= src.width && dst.height <= src.height){
                    return Rect.makeXYWH(
                        (src.width - dst.width) / 2,
                        (src.height - dst.height) / 2,
                        dst.width, dst.height
                    )
                }
                val scale = minOf(widthScale, heightScale)
                return Rect.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }
            None -> return Rect.makeXYWH(0f, 0f, dst.width, dst.height)
        }
    }
}