package element.measure.alignment

import element.measure.offset.FloatOffset
import element.measure.size.FloatSize

/**
 * 对齐方式布局
 *
 * 提供对齐方式的基本实现
 */
interface AlignmentLayout {
    var alignment: Alignment

    /**
     * 传入容器和元素的大小
     *
     * 输出元素在容器中的位置
     */
    fun alignment(src: FloatSize, dst: FloatSize): FloatOffset {
        return alignment(alignment, src, dst)
    }

    fun alignment(alignment: Alignment, src: FloatSize, dst: FloatSize): FloatOffset {
        val alignmentList = alignment.decode()
        val offset = FloatOffset()

        // 依次解析对齐方式
        alignmentList.forEach {
            when(it) {
                Alignment.TOP -> offset.y = 0f
                Alignment.BOTTOM -> offset.y = src.height - dst.height
                Alignment.LEFT -> offset.x = 0f
                Alignment.RIGHT -> offset.x = src.width - dst.width
                Alignment.CENTER -> {
                    offset.x = (src.width - dst.width) / 2
                    offset.y = (src.height - dst.height) / 2
                }
                Alignment.CENTER_HORIZONTAL -> offset.x = (src.width - dst.width) / 2
                Alignment.CENTER_VERTICAL -> offset.y = (src.height - dst.height) / 2
            }
        }

        return offset
    }
}