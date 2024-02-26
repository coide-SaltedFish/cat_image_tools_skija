package org.sereinfish.catcat.image.skiko.tools.demo.demo7

import org.jetbrains.skia.Color
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextFieldElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.utils.save

fun main() {
    val text = "  第一次马恩河战役(First Battle of the Marne)(法语：1re Bataille de la Marne) 又名马恩河奇迹(Miracle of Marne)是第一次世界大战西部战线的一次战役。这场战役发生在1914年9月5日至12日。在这场战役中，英法联军合力打败了德意志帝国军。\n" +
            "  第二次马恩河战役（Second Battle of the Marne）或称雷姆斯战役（Battle of Reims）是第一次世界大战西方战线发生于1918年7月15日至8月6日的战役，是西方战线中德军最后一次发动大规模攻击的战役。因为由法国军队领导的联盟军队反击，制服了德军，而德军遭受严重的伤亡。"

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .width(200)
//            .padding(5)
            .background(Color.WHITE),
        enableOpenGL = true
    ) {
        textField(
            modifier = Modifier<TextFieldElement>()
                .maxWidth(),
            subModifier = Modifier<TextElement>()
                .textShadow(1, 2, Color.RED)
                .padding(4),
            lineSpace = 5,
            typeface = Typeface.makeFromFile("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/font/MiSans-Normal.ttf"),
            fontSize = 28,
            text = text
        )
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo7/output")
}