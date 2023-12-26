import draw.draws.ElementColorDraw
import draw.draws.ElementStrokeDraw
import draw.effect.rRectShape
import draw.utils.blurBackground
import draw.utils.buildDraw
import element.context.ElementDrawContext
import element.elements.*
import element.elements.layout.AbsoluteLayout
import element.elements.layout.ColumLayout
import element.elements.layout.RowLayout
import element.measure.CropMode
import element.measure.ElementSizeMode
import element.measure.ShadowInfo
import element.measure.alignment.Alignment
import element.measure.offset.FloatOffset
import element.measure.size.FloatRectSize
import element.measure.size.FloatSize
import org.jetbrains.skia.*
import utils.*
import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {

//    canvas.drawRect(Rect.makeWH(size.width, size.height), Color.BLUE)

//    val text = TextElement("Hello World || g 你好", font = Font(Typeface.makeFromName("黑体", FontStyle.BOLD_ITALIC), 38f), shadow = ShadowInfo(1f, 2f, Color.RED))
////    text.beforeDrawChain.plus(buildDraw { canvas1, context ->
////        canvas1.drawRect(Rect.makeWH(size.width, size.height), Color.BLUE)
////    })
//    text.attributes.padding = FloatRectSize(20f, 20f, 20f, 20f)
//
//    text.updateElementInfo()
//
//    text.draw(ElementDrawContext(canvas))
//    val textSize = text.size
//    canvas.drawRect(Rect.makeWH(size.width, size.height), Rect.makeWH(textSize.width, textSize.height), utils.paint {
//        color = Color.RED
//        mode = PaintMode.STROKE
//        strokeWidth = 1f
//    })

    val imageFile = FileInputStream("C:\\Users\\MiaoTiao\\Pictures\\Saved Pictures\\5215dc5d0ed6edc331b8ed03d0df2992.jpg").use {
        Image.makeFromEncoded(it.readBytes())
    }

    val layout = ColumLayout(alignment = Alignment.RIGHT.and(Alignment.TOP)).apply {
        padding = FloatRectSize(left = 20f)

        add(ImageElement(imageFile, cropMode = CropMode.Fit, samplingMode = SamplingMode.CATMULL_ROM).apply {
            size = FloatSize(150, 150)
            sizeMode = ElementSizeMode.Value

            blurBackground(20f)
            rRectShape(20, stroke = true, strokeColor = Color.makeRGB(0x66, 0xcc, 0xff), antiAlias = true)
        })
        add(TextElement("123456", alignment = Alignment.CENTER_VERTICAL.and(Alignment.RIGHT)).apply {
            size.height = 50f
            sizeMode = ElementSizeMode.ValueHeight.and(ElementSizeMode.MaxWidth)

//            beforeDrawChain.plus(ElementColorDraw(this, Color.RED.colorCopy(a = 100)))
            padding = FloatRectSize(7)
//            blurBackground(5f)
            rRectShape(radius = 25, padding = true, antiAlias = true, shadowInfo = ShadowInfo(0, 3, Color.BLACK))
//            afterDrawChain.plus(ElementStrokeDraw(this))
        })
    }

    layout.add(AbsoluteLayout().apply {
        size = FloatSize(200, 200)
        sizeMode = ElementSizeMode.Value

        add(TextElement("底部"), AbsoluteLayout.OffsetInfo(alignment = Alignment.BOTTOM))
        add(TextElement("居中"), AbsoluteLayout.OffsetInfo(alignment = Alignment.CENTER))

        add(TextElement("底部居中"), AbsoluteLayout.OffsetInfo(alignment = Alignment.BOTTOM.and(Alignment.CENTER_HORIZONTAL)))

        add(TextElement("右边居中"), AbsoluteLayout.OffsetInfo(alignment = Alignment.RIGHT.and(Alignment.CENTER_VERTICAL)))

        add(TextElement("( 20,20)").apply {
            blurBackground(8f)
        }, AbsoluteLayout.OffsetInfo(FloatOffset(20f, 20f)))

        add(TextElement("( 20, 40), w=max").apply {
            attributes["text"] = 1

            sizeMode = ElementSizeMode.Auto.and(ElementSizeMode.MaxWidth)
            padding = FloatRectSize(5)

            blurBackground(10f)

            rRectShape(14, stroke = true, strokeColor = Color.BLACK.colorCopy(a = 100))
        }, AbsoluteLayout.OffsetInfo(FloatOffset(20f, 40f)))

        afterDrawChain.plus(ElementStrokeDraw(this, color = Color.BLUE))
    })

    layout.add(RowLayout(alignment = Alignment.CENTER).apply {
        padding = FloatRectSize(left = 20f)

        sizeMode = ElementSizeMode.Auto

        beforeDrawChain.plus(ElementColorDraw(this, Color.BLACK.colorCopy(a = 70)))

        blurBackground(5f)

        add(RectElement(10, 10, Color.BLACK).apply {
            padding = FloatRectSize(10f, 10f, 10f, 10f)

            shadow(ShadowInfo(2f, 2f, 5f, Color.BLACK))
        })

        add(ArcElement(Rect.makeWH(20f, 50f), 0f, 90f, false){
            color = Color.BLUE
        }.apply {
            padding = FloatRectSize(10f, 10f, 10f, 10f)

            shadow(ShadowInfo(2f, 2f, 1f, Color.BLACK))
        })
        add(ArcElement(Rect.makeWH(20f, 50f), 0f, 90f, true){
            color = Color.RED
        }.apply {
            padding = FloatRectSize(10f, 10f, 0f, 0f)
        })

        add(ArcElement.circular(30f){
            color = Color.GREEN
        }.apply {
            padding = FloatRectSize(10f, 10f, 10f, 10f)

            shadow(ShadowInfo(2f, 2f, 5f, Color.BLACK))
        })

        add(ArcElement.circular(30f){
            color = Color.BLACK.colorCopy(a = 10)
        }.apply {
            padding = FloatRectSize(10f, 10f, 10f, 10f)
            // TODO 实现形状阴影通用接口
            shadow(ShadowInfo(2f, 2f, 5f, Color.BLACK))
        })

        add(RRectElement(100f, 50f, 5f, 8f){
            color = Color.RED
        }.apply {
            padding = FloatRectSize(10)

            shadow(ShadowInfo(2, 5, Color.BLACK))
        })

        rRectShape(blRad = 20, brRad = 20, antiAlias = true, stroke = true)
    })

    val startTime = System.currentTimeMillis()

    val absoluteLayout = AbsoluteLayout()
    absoluteLayout.size = imageFile.size() / 2

    absoluteLayout.add(ImageElement(imageFile, cropMode = CropMode.FitFill, samplingMode = SamplingMode.CATMULL_ROM).apply {
        sizeMode = ElementSizeMode.MaxFill
    })
    absoluteLayout.add(layout.apply {
        sizeMode = ElementSizeMode.Auto
    })

    absoluteLayout.updateElementInfo()

    val image = Surface.makeRasterN32Premul(absoluteLayout.size.width.toInt(), absoluteLayout.size.height.toInt())

    absoluteLayout.draw(ElementDrawContext(image))

    val time = System.currentTimeMillis() - startTime
    println("耗时：$time ms")


    val data = image.makeImageSnapshot().encodeToData(EncodedImageFormat.PNG)!!
    FileOutputStream("./output.png").use {
        it.write(data.bytes)
        it.flush()
    }
}