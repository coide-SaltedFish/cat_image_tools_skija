package org.sereinfish.catcat.image.skiko.tools.utils.chain

/**
 * 双向链表
 */
open class DoublyLinkedList<T>: Iterable<T> {
    private var head: DoublyLinkedNode<T>? = null
    private var tail: DoublyLinkedNode<T>? = null

    fun isEmpty() = head == null

    fun append(value: T): DoublyLinkedList<T> {
        val node = DoublyLinkedNode(value)
        if (head == null) head = node

        tail?.next = node
        node.prev = tail
        tail = node
        return this
    }

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private var current = head
            override fun hasNext(): Boolean = current != null
            override fun next(): T {
                val result = current?.value
                current = current?.next
                return result ?: throw NoSuchElementException("No more elements in the list.")
            }
        }
    }
}