package org.sereinfish.catcat.image.skiko.tools.demo.demo2

import org.jetbrains.skia.*
import org.sereinfish.catcat.image.skiko.tools.build.buildImageColumLayout
import org.sereinfish.catcat.image.skiko.tools.build.extend.*
import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.draw.utils.buildDraw
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.elements.ImageElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.RectElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.ShapeElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.TextElement
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.AbsoluteLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.sereinfish.catcat.image.skiko.tools.element.measure.CropMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.ShadowInfo
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.Alignment
import org.sereinfish.catcat.image.skiko.tools.element.measure.alignment.and
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.*

/**
 * 示例2
 *
 * 构建一个简单的签到卡片
 * 可能会有一些难以理解为什么这样的代码，这是为了方便理解，绝对不是因为写了使山代码我懒得改
 */
fun main() {
    val imageFile = Image.makeFromEncoded("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/src/image/p_114750072.png")

    buildImageColumLayout(
        modifier = Modifier<ColumLayout>()
            .alignment(Alignment.CENTER_VERTICAL)
            .width(800)
            .padding(20)
            .rRectShape(30, stroke = true, strokeColor = Color.BLACK.colorCopy(a = 50), padding = padding(10))
            .background(Color.WHITE)
    ) {
        // 用户基本信息
        abs(
            modifier = Modifier<AbsoluteLayout>()
                .rRectShape(10, shadowInfo = ShadowInfo(0, 2, Color.BLACK.colorCopy(a = 150)), padding = padding(10))
                .maxWidth()
        ) {
            p place image(
                image = imageFile,
                offset = offset(x = - imageFile.width / 4, y = imageFile.height / 5),
                cropMode = CropMode.FillWidth,
                alignment = Alignment.CENTER,
                samplingMode = SamplingMode.CATMULL_ROM,
                modifier = Modifier<ImageElement>()
                    .maxSize()
                    .imageBlur(3)
            )

            p place rect(
                modifier = Modifier<RectElement>()
                    .maxSize(),
                size = FloatSize(),
                color = Color.WHITE,
                paintBuilder = {
                    shader = Shader.makeLinearGradient(
                        x0 = it.size.width / 3, y0 = 0.0f, x1 = it.size.width, y1 = 0.0f,
                        colors = intArrayOf(Color.WHITE, Color.TRANSPARENT),
                        positions = floatArrayOf(0f, 1f)
                    )
                }
            )

            p place row (
                modifier = Modifier<RowLayout>()
                    .alignment(Alignment.CENTER_VERTICAL and Alignment.LEFT)
                    .maxWidth()
                    .padding(30)
                    .height(220)
            ) {
                // 头像
                image(
                    modifier = Modifier<ImageElement>()
                        .size(140)
                        .circularShape(stroke = true, strokeColor = Color.BLACK.colorCopy(a = 120)),
                    image = imageFile,
                    cropMode = CropMode.FillWidth,
                    alignment = Alignment.TOP,
                    samplingMode = SamplingMode.CATMULL_ROM
                )
                row(Modifier<RowLayout>().size(30))
                userNameInfo(
                    Modifier(),
                    "一只大猫条",
                    "只是一个路过的猫猫罢了"
                )
            }
        }

        // 一些详情
        abs(
            modifier = Modifier<AbsoluteLayout>()
                .maxWidth()
        ) {
            p place shape(
                modifier = Modifier<ShapeElement>()
                    .maxSize()
                    .padding(5)
                    .background {
                        val width = it.size.width - it.padding.width
                        val height = it.size.height - it.padding.height

                        saveBlock({
                            translate(it.padding.left, it.padding.top)
                            clipPath(path {
                                moveTo(0f, 0f)
                                lineTo(width - 20, 0f)
                                lineTo(width , height)
                                lineTo(20f, height)
                                lineTo(0f, 0f)
                            })
                        }) {
                            drawPath(path {
                                for (yi in 0..< height.toInt() step 10){
                                    for(xi in 0 ..< width.toInt() / 20){
                                        if (xi % 2 == 0){
                                            addRect(Rect.makeXYWH(xi.toFloat() * 20, yi.toFloat(), 10f, 10f))
                                        }else {
                                            addRect(Rect.makeXYWH(xi.toFloat() * 20 + 10f, yi.toFloat(), 10f, 10f))
                                        }
                                    }
                                }
                            }, paint {
                                color = Color.makeRGB(0x66, 0xcc, 0xff)
                            })
                        }
                    }
                    .blurBackground(8,
                        clipPath = {
                            val width = it.size.width - it.padding.width
                            val height = it.size.height - it.padding.height

                            moveTo(0f, 0f)
                            lineTo(width - 20, 0f)
                            lineTo(width , height)
                            lineTo(20f, height)
                            lineTo(0f, 0f)

                            transform(Matrix33.makeTranslate(it.padding.left, it.padding.top))
                        }
                    ),
                path = {
                    val width = it.size.width - it.padding.width
                    val height = it.size.height - it.padding.height

                    moveTo(0f, 0f)
                    lineTo(width - 20, 0f)
                    lineTo(width , height)
                    lineTo(20f, height)
                    lineTo(0f, 0f)
                },
                shadowInfo = ShadowInfo(1, 3, Color.BLACK.colorCopy(a = 50)),
                paint = {
                    mode = PaintMode.STROKE
                    color = Color.makeRGB(100, 100, 100)
                }
            )
            p place colum(
                modifier = Modifier<ColumLayout>()
                    .maxWidth()
                    .padding(left = 30, top = 10, right = 30, bottom = 10)
            ) {
                text(text = "也许这里可以写一些个人信息描述？", fontName = "宋体")
                text(modifier = Modifier<TextElement>().padding(top = 10, bottom = 10), text = "也许这里可以写一些个人信息描述？", fontName = "宋体")
                text(text = "也许这里可以写一些个人信息描述？", fontName = "宋体")
            }
        }
    }.save("./src/main/kotlin/org/sereinfish/catcat/image/skiko/tools/demo/demo2/output")
}

/**
 * 用户名字信息卡片
 */
private fun Layout.userNameInfo(
    modifier: Modifier<ColumLayout> = Modifier(),
    name: String,
    desc: String
) = colum {
    text(
        modifier = Modifier<TextElement>()
            .padding(3),
        text = name,
        fontStyle = FontStyle.ITALIC,
        fontSize = 43,
        wordSpace = 8,
        shadow = ShadowInfo(3, 1, Color.BLACK.colorCopy(a = 120))
    )
    colum(modifier = Modifier<ColumLayout>().size(20))
    text(
        modifier = Modifier<TextElement>()
            .padding(2),
        text = desc,
        typeface = Typeface.makeFromName("微软雅黑", FontStyle.NORMAL),
        fontSize = 20,
        wordSpace = 3,
        color = Color.makeRGB(100, 100, 100),
        shadow = ShadowInfo(0, 1, Color.BLACK.colorCopy(a = 120))
    )
}