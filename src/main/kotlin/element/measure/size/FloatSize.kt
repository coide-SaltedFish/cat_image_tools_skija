package element.measure.size

import element.measure.BasicOperations

data class FloatSize(
    var width: Float = 0f,
    var height: Float = 0f,
): BasicOperations<Number, FloatSize> {

    constructor(w: Int, h: Int): this(w.toFloat(), h.toFloat())

    fun add(size: FloatSize): FloatSize {
        width += size.width
        height += size.height
        return this
    }

    fun minus(size: FloatSize): FloatSize {
        width -= size.width
        height -= size.height
        return this
    }

    /**
     * 确保大小不会小于0
     */
    fun nonNegativeValue(): FloatSize {
        width = maxOf(0f, width)
        height = maxOf(0f, height)

        return this
    }

    override operator fun div(other: Number): FloatSize {
        width /= other.toFloat()
        height /= other.toFloat()
        return this
    }

    override fun plus(other: Number): FloatSize {
        width += other.toFloat()
        height += other.toFloat()
        return this
    }

    override fun minus(other: Number): FloatSize {
        width -= other.toFloat()
        height -= other.toFloat()
        return this
    }

    override fun times(other: Number): FloatSize {
        width *= other.toFloat()
        height *= other.toFloat()
        return this
    }

    override fun rem(other: Number): FloatSize {
        width %= other.toFloat()
        height %= other.toFloat()
        return this
    }

    override fun toString(): String {
        return "FloatSize(width=$width, height=$height)"
    }
}
