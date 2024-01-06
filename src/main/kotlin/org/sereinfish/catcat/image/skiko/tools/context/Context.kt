package org.sereinfish.catcat.image.skiko.tools.context

import org.sereinfish.catcat.image.skiko.tools.context.property.ContextProperty
import org.sereinfish.catcat.image.skiko.tools.context.property.ContextPropertyOrDefault
import org.sereinfish.catcat.image.skiko.tools.context.property.ContextPropertyOrElse
import org.sereinfish.catcat.image.skiko.tools.context.property.ContextPropertyOrNull

/**
 * 一个基本的上下文
 */
open class Context(
    private val context: HashMap<String, Any?> = LinkedHashMap()
) {

    fun getContext() = context

    operator fun get(key: String) = context[key]
    operator fun set(key: String, value: Any?){
        context[key] = value
    }

    inline fun <reified T> value() = ContextProperty<T>(this, T::class.java)
    inline fun <reified T> valueOrNull() = ContextPropertyOrNull<T>(this, T::class.java)
    inline fun <reified T> valueOrDefault(default: T) = ContextPropertyOrDefault(this, default, T::class.java)
    inline fun <reified T> valueOrElse(noinline default: () -> T) = ContextPropertyOrElse(this, default, T::class.java)
}