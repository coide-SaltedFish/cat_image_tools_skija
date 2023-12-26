package element.measure

import element.measure.CropMode.*
import element.measure.offset.FloatOffset
import element.measure.size.FloatRectSize
import element.measure.size.FloatSize

/**
 * 使用裁剪参数的布局
 */
interface CropLayout {
    var cropMode: CropMode

    /**
     * 输入容器和元素大小
     * 计算对应裁剪方式下子布局的位置
     *
     * 返回对应的元素选取区域和放置到布局上的区域位置
     */
    fun crop(src: FloatSize, dst: FloatSize): Pair<FloatRectSize, FloatRectSize> {
        // 计算宽高的缩放比例
        val widthScale = src.width / dst.width
        val heightScale = src.height / dst.height

        when(cropMode){
            Fit -> { // 适应长边
                val scale = minOf(widthScale, heightScale)

                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }

            FitFill -> {
                val scale = maxOf(widthScale, heightScale)

                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }

            Crop -> {
                // 如果图片整体小于容器，则居中返回
                if (dst.width <= src.width && dst.height <= src.height){
                    return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                        (src.width - dst.width) / 2,
                        (src.height - dst.height) / 2,
                        dst.width, dst.height
                    )
                }
                // 找到缩放比例高的一边
                val scale = maxOf(widthScale, heightScale)
                // 计算要选取的图片区域
                return FloatRectSize.makeXYWH(
                    (dst.width - src.width * scale) / 2,
                    (dst.height - src.height * scale) / 2,
                    src.width, src.height
                ) to FloatRectSize.makeXYWH(0f, 0f, src.width, src.height)
            }

            FillHeight -> {
                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    (src.width - dst.width * heightScale) / 2,
                    (src.height - dst.height * heightScale) / 2,
                    dst.width * heightScale, dst.height * heightScale
                )
            }
            FillWidth -> {
                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    (src.width - dst.width * widthScale) / 2,
                    (src.height - dst.height * widthScale) / 2,
                    dst.width * widthScale, dst.height * widthScale
                )
            }
            FillBounds -> {
                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    0f, 0f, dst.width * widthScale, dst.height * heightScale
                )
            }
            Inside -> {
                // 如果图片整体小于容器，则居中返回
                if (dst.width <= src.width && dst.height <= src.height){
                    return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                        (src.width - dst.width) / 2,
                        (src.height - dst.height) / 2,
                        dst.width, dst.height
                    )
                }
                val scale = minOf(widthScale, heightScale)
                return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(
                    (src.width - dst.width * scale) / 2,
                    (src.height - dst.height * scale) / 2,
                    dst.width * scale, dst.height * scale
                )
            }
            None -> return FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height) to FloatRectSize.makeXYWH(0f, 0f, dst.width, dst.height)
        }
    }
}