package org.sereinfish.catcat.image.skiko.tools.demo.demo5

import org.jetbrains.skia.Color
import org.jetbrains.skia.Image
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.ImageElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.utils.forEachWithSeparator
import org.sereinfish.catcat.image.skiko.tools.utils.makeFromEncoded
import org.sereinfish.catcat.image.skiko.tools.utils.save

/**
 * 图片裁剪示例
 */
fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_106009822.jpg")

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .background(Color.WHITE)
            .padding(20)
    ) {
        CropMode.entries.forEachWithSeparator({
            row(modifier = Modifier<RowLayout>().size(20))
        }) {
            text(modifier = Modifier<TextElement>().padding(tb = 5), text = it.name)
            colum (
                modifier = Modifier<ColumLayout>()
                    .size(200)
                    .border()
            ) {
                image(
                    modifier = Modifier<ImageElement>()
                        .maxSize(),
                    cropMode = it,
                    image = imageFile
                )
            }
        }
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo5/output")
}