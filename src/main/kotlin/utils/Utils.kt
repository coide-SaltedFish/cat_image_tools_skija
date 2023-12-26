package utils

import element.measure.size.FloatSize
import org.jetbrains.skia.Image

inline fun CharSequence.sumOf(selector: (Char) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun CharSequence.sumOfIndex(selector: (Int) -> Float): Float {
    var sum = 0f
    for (element in this.indices) {
        sum += selector(element)
    }
    return sum
}

/**
 * 增强版forEach
 * 在两个元素遍历中间增加逻辑处理
 */
inline fun <T> Iterable<T>.forEachWithSeparator(separator: (T) -> Unit = {}, action: (T) -> Unit){
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val value = iterator.next()
        action(value)
        if (iterator.hasNext()) {
            separator(value)
        }
    }
}

/**
 * 增强版forEach
 * 满足指定条件时提前结束遍历
 */
inline fun <T> Iterable<T>.forEachOrEnd(endFunc: (T) -> Boolean, action: (T) -> Unit){
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val value = iterator.next()
        if (endFunc(value)) {
            break
        }
        action(value)
    }
}



inline fun <T> LinkedHashSet<T>.sumOf(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

inline fun <T> LinkedHashSet<T>.sumOrEnd(endFunc: (T) -> Boolean, selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        if (endFunc(element)) {
            break
        }
        sum += selector(element)
    }
    return sum
}

/**
 * 图片的大小
 */
fun Image.size() = FloatSize(width.toFloat(), height.toFloat())