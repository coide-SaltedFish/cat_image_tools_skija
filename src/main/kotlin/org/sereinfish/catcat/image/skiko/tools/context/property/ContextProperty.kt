package org.sereinfish.catcat.image.skiko.tools.context.property

import org.sereinfish.catcat.image.skiko.tools.context.Context
import kotlin.reflect.KProperty

class ContextProperty<T> (
    private val context: Context, // 上下文
    private val type: Class<*>
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value = context[property.name] ?: throw NullPointerException("上下文找不到对应属性 ${property.name} :null")
        return value as? T ?: error("无法将类型[${value::class.java.name}]赋值到属性${property.name}[${type.name}]")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        context[property.name] = value
    }
}

class ContextPropertyOrNull<T> (
    val context: Context, // 上下文
    val type: Class<*>,
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val value = context[property.name] ?: return null
        return value as? T ?: error("无法将类型[${value::class.java.name}]赋值到属性${property.name}[${type.name}]")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        context[property.name] = value
    }
}

class ContextPropertyOrDefault<T> (
    val context: Context, // 上下文
    val defaultValue: T, // 默认值
    val type: Class<*>,
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value = context[property.name] ?: return defaultValue
        return value as? T ?: error("无法将类型[${value::class.java.name}]赋值到属性${property.name}[${type.name}]")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        context[property.name] = value
    }
}

class ContextPropertyOrElse<T> (
    val context: Context, // 上下文
    val default: () -> T, // 默认值
    val type: Class<*>,
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val value = context[property.name] ?: return default().also {
            context[property.name] = it // 保存结果
        }
        return (value as? T)?.also {
            context[property.name] = it // 保存结果
        } ?: error("无法将类型[${value::class.java.name}]赋值到属性${property.name}[${type.name}]")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Any?) {
        context[property.name] = value
    }
}