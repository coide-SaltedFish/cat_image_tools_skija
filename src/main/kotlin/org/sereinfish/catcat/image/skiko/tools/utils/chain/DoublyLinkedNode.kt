package org.sereinfish.catcat.image.skiko.tools.utils.chain

class DoublyLinkedNode<T>(
    var value: T
) {
    var prev: DoublyLinkedNode<T>? = null
    var next: DoublyLinkedNode<T>? = null
}