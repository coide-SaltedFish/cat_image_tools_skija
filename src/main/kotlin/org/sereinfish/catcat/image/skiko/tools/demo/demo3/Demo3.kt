package org.sereinfish.catcat.image.skiko.tools.demo.demo3

import org.jetbrains.skia.Color
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.RectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.ShapeElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.utils.forEachWithSeparator
import org.sereinfish.catcat.image.skiko.tools.utils.save


/**
 * 构建一个设置菜单
 */
fun main() {
    val items = arrayOf(
        arrayOf("我的设备"),
        arrayOf("双卡和移动网络", "WLAN", "蓝牙", "个人热点", "VPN", "连接与共享"),
        arrayOf("壁纸与个性化", "显示", "声音和振动", "通知和控制中心"),
        arrayOf("安全", "隐私保护", "省电与电池", "应用设置"),
    )

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .size(1080, 1920)
            .padding(left = 50, right = 50, top = 40)
            .background(Color.WHITE)
    ) {
        backBtn(Modifier<ShapeElement>().size(50, 30), strokeWidth = 3)
        text(
            modifier = Modifier<TextElement>()
                .padding(tb = 50),
            text = "设置",
            typeface = Typeface.makeFromFile("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/font/MiSans-Normal.ttf"),
            fontSize = 72
        )
        // 菜单项
        items.forEachWithSeparator({
            line(color = Color.makeRGB(100, 100, 100), width = 3)
        }) {
            it.forEach { itemName ->
                item(text = itemName)
            }
        }

    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo3/output")
}

private fun ColumLayout.item(
    modifier: Modifier<ColumLayout> = Modifier<ColumLayout>()
        .maxWidth()
        .alignment(Alignment.CENTER_VERTICAL)
        .padding(lr = 20, tb = 30),
    text: String,
    block: ColumLayout.() -> Unit = {}
): ColumLayout {
    val layout = ColumLayout()
    modifier.modifier(layout)
    layout.block()
    layout.text(
        modifier = Modifier<TextElement>(),
        text = text,
        wordSpace = 8f,
        typeface = Typeface.makeFromFile("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/font/MiSans-Normal.ttf"),
        fontSize = 52
    )

    add(layout)
    return layout
}

private fun Layout.backBtn(modifier: Modifier<ShapeElement>, strokeWidth: Number = 1) = shape(
    modifier = modifier,
    path = {
        val s = it.size.copy().minus(it.padding.size())
        moveTo(s.width / 3, 0f)
        lineTo(0f, s.height / 2)
        lineTo(s.width / 3, s.height)
        moveTo(0f, s.height / 2)
        lineTo(s.width, s.height / 2)
    },
    paint = {
        color = Color.BLACK
        mode = PaintMode.STROKE
        this.strokeWidth = strokeWidth.toFloat()
    }
)

private fun Layout.line(color: Int, width: Number) = rect(
    modifier = Modifier<RectElement>()
        .padding(lr = 20, tb = 20)
        .maxWidth(),
    height = width,
    color = color
)