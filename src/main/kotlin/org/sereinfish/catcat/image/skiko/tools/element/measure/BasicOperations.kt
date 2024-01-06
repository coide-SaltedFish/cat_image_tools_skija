package org.sereinfish.catcat.image.skiko.tools.element.measure

/**
 * 一些基本运算
 */
interface BasicOperations<N, T> {
    operator fun plus(other: N): T
    operator fun minus(other: N): T
    operator fun times(other: N): T
    operator fun div(other: N): T
    operator fun rem(other: N): T
}