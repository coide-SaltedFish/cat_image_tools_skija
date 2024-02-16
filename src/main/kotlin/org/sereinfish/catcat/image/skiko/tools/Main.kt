package org.sereinfish.catcat.image.skiko.tools

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.colum
import org.sereinfish.catcat.image.skiko.tools.build.extend.image
import org.sereinfish.catcat.image.skiko.tools.build.extend.rect
import org.sereinfish.catcat.image.skiko.tools.build.extend.size
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.AbstractElement
import org.sereinfish.catcat.image.skiko.tools.element.Element
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.utils.makeFromEncoded
import org.sereinfish.catcat.image.skiko.tools.utils.save

fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_106009822.jpg")

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .size(imageFile.width, imageFile.height)
    ) {
        image(image = imageFile, paintBuilder = {
            blendMode = BlendMode.SRC_IN
        })

        beforeDrawChain.plus(buildDraw {
            val shader = Shader.makeLinearGradient(
                0f, 0f,  // 渐变的起始位置
                imageFile.width.toFloat(), 0f,  // 渐变的结束位置
                intArrayOf(0xFFFFFFFF.toInt(), 0x00FFFFFF),  // 渐变的颜色数组
                null,  // 渐变的位置数组，null表示颜色在起始和结束位置均匀分布
            )

            // 创建一个新的画笔，并设置其着色器为我们刚刚创建的渐变
            val paint = Paint().apply {
                this.shader = shader
            }
            drawImage(imageFile, 0f, 0f)
        })
    }.save("./output")
}