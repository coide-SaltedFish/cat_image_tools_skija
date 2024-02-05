package org.sereinfish.catcat.image.skiko.tools.demo.demo4

import org.jetbrains.skia.Color
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.RectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.utils.random
import org.sereinfish.catcat.image.skiko.tools.utils.save

/**
 * 比例布局
 */
fun main() {
    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .size(1000, 1000)
            .padding(50)
            .background(Color.WHITE)
    ) {
        row(
            Modifier<RowLayout>()
                .maxWidth()
                .height(200)
        ) {
            repeat(5){
                rect(
                    Modifier<RectElement>()
                        .weight(1)
                        .maxSize(),
                    color = Color.random()
                )
            }
        }

        repeat(5){
            rect(
                Modifier<RectElement>()
                    .weight(1)
                    .maxSize(),
                color = Color.random()
            )
        }
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo4/output")
}