package org.sereinfish.catcat.image.skiko.tools.demo.demo6

import org.jetbrains.skia.Color
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.utils.save

fun main() {
    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .size(150, 150)
            .background(Color.WHITE)
    ) {
        text(
            modifier = Modifier<TextElement>()
                .background(Color.YELLOW)
                .maxWidth(70),
            text = "maxWidth=70"
        )

        text(
            modifier = Modifier<TextElement>()
                .background(Color.BLUE)
                .minWidth(140),
            text = "minWidth=140"
        )
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo6/output")
}