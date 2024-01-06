package org.sereinfish.catcat.image.skiko.tools.build.modifier

import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.utils.chain.DoublyLinkedList

interface ModifierNode<T> {
    fun modifier(element: T)
}

/**
 * 元素构建器
 */
class Modifier<T: Element>: DoublyLinkedList<ModifierNode<T>>(){
    /**
     * 进行元素处理
     */
    fun modifier(element: T){
        forEach {
            it.modifier(element)
        }
    }

    /**
     * 新增构建器
     */
    fun modifier(block: T.() -> Unit): Modifier<T> {
        append(object : ModifierNode<T> {
            override fun modifier(element: T) {
                block(element)
            }
        })

        return this
    }
}