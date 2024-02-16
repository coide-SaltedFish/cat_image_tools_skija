package org.sereinfish.catcat.image.skiko.tools

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.buildImageRowLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.colorCopy
import org.sereinfish.catcat.image.skiko.tools.utils.makeFromEncoded
import org.sereinfish.catcat.image.skiko.tools.utils.save

fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_106009822.jpg")

    buildImageRowLayout(
        modifier = Modifier<RowLayout>()
            .width(500)
    ) {
        rect(size = FloatSize(100, 100), color = Color.RED)

        colum(
            modifier = Modifier<ColumLayout>()
                .maxSize()
                .background(Color.BLUE.colorCopy(a = 100))
        ) {

        }
    }.save("./output")
}