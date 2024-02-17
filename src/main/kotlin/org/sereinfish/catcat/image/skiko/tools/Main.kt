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
import org.sereinfish.catcat.image.skiko.tools.element.elements.RectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.colorCopy
import org.sereinfish.catcat.image.skiko.tools.utils.makeFromEncoded
import org.sereinfish.catcat.image.skiko.tools.utils.save

fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_106009822.jpg")

    buildImageRowLayout(
        modifier = Modifier<RowLayout>()
            .size(500, 100)
    ) {
        colum(
            modifier = Modifier<ColumLayout>()
                .maxHeight()
                .padding(5)
                .rRectShape(
                    10,
                    stroke = true,
                    strokeColor = Color.BLACK.colorCopy(a = 50),
                )
                .square()
        ) {
            colum(
                modifier  = Modifier<ColumLayout>()
                    .maxSize()
                    .weight(1)
            ) {
                text(
                    modifier  = Modifier<TextElement>()
                        .alignment(Alignment.CENTER)
                        .maxSize(),
                    text = "123",
                    fontSize = 1f,
                    enableAdaptiveFontSize = true
                )
            }

            colum(
                modifier  = Modifier<ColumLayout>()
                    .maxSize()
                    .weight(1)
            ) {
                text(
                    modifier  = Modifier<TextElement>()
                        .alignment(Alignment.CENTER)
                        .maxSize(),
                    text = "Lv",
                    fontSize = 1f,
                    enableAdaptiveFontSize = true
                )
            }
        }
    }.save("./output")
}