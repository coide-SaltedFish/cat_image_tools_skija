package org.sereinfish.catcat.image.skiko.tools.element.measure.offset

import org.sereinfish.catcat.image.skiko.tools.element.measure.BasicOperations

data class FloatOffset(
    var x: Float = 0f,
    var y: Float = 0f,
): BasicOperations<Number, FloatOffset> {
    constructor(x: Number, y: Number): this(x.toFloat(), y.toFloat())

    fun add(offset: FloatOffset): FloatOffset {
        x += offset.x
        y += offset.y

        return this
    }

    override fun plus(other: Number): FloatOffset {
        x += other.toFloat()
        y += other.toFloat()
        return this
    }

    override fun minus(other: Number): FloatOffset {
        x -= other.toFloat()
        y -= other.toFloat()
        return this
    }

    override fun times(other: Number): FloatOffset {
        x *= other.toFloat()
        y *= other.toFloat()
        return this
    }

    override fun div(other: Number): FloatOffset {
        x /= other.toFloat()
        y /= other.toFloat()
        return this
    }

    override fun rem(other: Number): FloatOffset {
        x %= other.toFloat()
        y %= other.toFloat()
        return this
    }

    override fun toString(): String {
        return "FloatOffset(x=$x, y=$y)"
    }
}
