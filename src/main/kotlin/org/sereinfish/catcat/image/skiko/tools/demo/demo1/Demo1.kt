import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.*
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.AbsoluteLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.utils.*

/**
 * 一个基本的测试用例
 */

fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_106009822.jpg")

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .background(Color.RED)
            .background(imageFile)
            .background(Color.BLACK.colorCopy(a = 40))
            .blurBackground(4f)
            .size(500, 500)
            .alignment(Alignment.CENTER_HORIZONTAL and Alignment.BOTTOM)
    ) {

        image(
            modifier = Modifier<ImageElement>()
                .size(100)
                .circularShape(
                    antiAlias = true,
                    shadowInfo = ShadowInfo(2, 5, Color.BLACK),
                    padding = padding(10)
                ),
            image = imageFile,
            cropMode = CropMode.FillWidth,
            samplingMode = SamplingMode.CATMULL_ROM,
            alignment = Alignment.BOTTOM
        )

        abs(
            modifier = Modifier<AbsoluteLayout>()
                .size(200)
                .padding(10)
                .rRectShape(
                    10,
                    stroke = true,
                    strokeColor = Color.YELLOW,
                    antiAlias = true,
                    shadowInfo = ShadowInfo(2, 5, Color.BLACK),
                    padding = padding(10)
                )
        ) {
            BOTTOM and RIGHT place text(text = "底部靠右")

            text(text = "居中") on CENTER

            text(
                modifier = Modifier<TextElement>()
                    .alignment(CENTER)
                    .border(color = Color.RED),
                text = "[10, 150]"
            ) on p[10, 150]

            offset(10, 50) place text(text = "[10, 50]")

            + text(text = "Hello world")
        }

        colum(
            modifier = Modifier<ColumLayout>()
                .blurBackground(10)
                .size(50)
                .maxWidth()
        )

        image(
            modifier = Modifier<ImageElement>()
                .padding(5f)
                .size(100)
                .imageShadow(2f, 5f, Color.BLACK)
                .imageBlur(1f),
            image = imageFile,
        )

        text(
            modifier = Modifier<TextElement>()
                .padding(5)
                .textShadow(1, 2, Color.YELLOW)
                .border(color = Color.RED),
            text = "Hello World",
            fontSize = 32,
            color = Color.WHITE,
        )

        text(
            modifier = Modifier<TextElement>()
                .padding(2)
                .textShadow(0, 1, Color.RED)
                .blur(1),
            text = "123"
        )
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo1/output")
}