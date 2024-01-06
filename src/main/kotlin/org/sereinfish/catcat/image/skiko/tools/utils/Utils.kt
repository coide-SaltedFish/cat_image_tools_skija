package org.sereinfish.catcat.image.skiko.tools.utils

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.EncodedImageFormat.*
import org.jetbrains.skia.Image
import org.jetbrains.skia.Path
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatRectSize
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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

inline fun <T> Array<T>.forEachWithSeparator(separator: (T) -> Unit = {}, action: (T) -> Unit){
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

/**
 * 图片读取
 */
fun Image.Companion.makeFromEncoded(file: File) = FileInputStream(file).use {
    makeFromEncoded(it.readBytes())
}

fun Image.Companion.makeFromEncoded(path: String) = makeFromEncoded(File(path))

val EncodedImageFormat.extension: String get() = when(this){
    BMP -> "bmp"
    GIF -> "gif"
    ICO -> "ico"
    JPEG -> "jpeg"
    PNG -> "png"
    WBMP -> "wbmp"
    WEBP -> "webp"
    PKM -> "PKM"
    KTX -> "ktx"
    ASTC -> "astc"
    DNG -> "dng"
    HEIF -> "heif"
}

/**
 * 保存文件
 */
fun Image.save(f: File, format: EncodedImageFormat = PNG, quality: Int = 100): File {
    val file = if (f.extension.isEmpty()){
        // 添加对应后缀
        File("${f.absolutePath}.${format.extension}")
    }else f
    FileOutputStream(file).use {
        it.write(encodeToData(format, quality)!!.bytes)
        it.flush()
    }
    return file
}

fun Image.save(path: String, format: EncodedImageFormat = PNG, quality: Int = 100) = save(File(path), format, quality)

/**
 * 内边距
 */
inline fun padding(value: Number) = FloatRectSize(value)

inline fun padding(
    left: Number = 0f,
    top: Number = 0f,
    bottom: Number = 0f,
    right: Number = 0f,
) = FloatRectSize(left.toFloat(), right.toFloat(), top.toFloat(), bottom.toFloat())

/**
 * 构建一个路径对象
 */
inline fun path(block: Path.() -> Unit) = Path().apply(block)