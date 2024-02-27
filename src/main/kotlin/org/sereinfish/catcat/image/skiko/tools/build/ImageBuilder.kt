package org.sereinfish.catcat.image.skiko.tools.build

import org.sereinfish.catcat.image.skiko.tools.build.modifier.Modifier
import org.sereinfish.catcat.image.skiko.tools.element.Layout
import org.sereinfish.catcat.image.skiko.tools.element.context.ElementDrawContext
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.ColumLayout
import org.sereinfish.catcat.image.skiko.tools.element.elements.layout.RowLayout
import org.jetbrains.skia.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryUtil.NULL
import org.sereinfish.catcat.image.skiko.tools.element.measure.ElementSizeMode
import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.sereinfish.catcat.image.skiko.tools.utils.runTime
import java.lang.Error
import java.nio.IntBuffer
import kotlin.concurrent.thread
import kotlin.math.roundToInt

/**
 * 构建一个图片
 */
class ImageBuilder<T: Layout>(
    val layout: T = ColumLayout() as T, // 默认布局
    val modifier: Modifier<T>? = null,
    val colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    val enableOpenGL: Boolean = true,
    // 启用硬件加速有效
    val sampleCnt: Int = 0,
    val stencilBits: Int = 0,
    val fbId: Int = 0,
    val fbFormat: Int = FramebufferFormat.GR_GL_RGBA8,
    val origin: SurfaceOrigin = SurfaceOrigin.TOP_LEFT,
    val colorFormat: SurfaceColorFormat = SurfaceColorFormat.RGBA_8888,
    val colorSpace: ColorSpace? = ColorSpace.sRGB,
    val surfaceProps: SurfaceProps? = null
){
    companion object {
        var window: Long = NULL
        var context: DirectContext? = null

        /**
         * 初始化Lwjgl
         *
         * 完成窗口初始化
         * 上下文构建
         */
        private fun initGLFW(){
            GLFWErrorCallback.createPrint(System.err).set()
            check(glfwInit()) { "Unable to initialize GLFW" }
            glfwDefaultWindowHints()
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

            window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
            if (window == NULL) throw RuntimeException("Failed to create the GLFW window")

            glfwMakeContextCurrent(window)
            GL.createCapabilities()
        }

        fun setWindowSize(size: FloatSize){
            if (window == NULL) initGLFW()

            glfwSetWindowSize(window, size.width.roundToInt(), size.height.roundToInt())
        }

        fun <T> glBlock(block: () -> T): T {
            if (window == NULL) initGLFW()

            return block()
        }

        fun <T> makeGL(block: DirectContext.() -> T): T {
            return glBlock {
                context = context ?: DirectContext.makeGL()
                block(context ?: error("Unable to initialize DirectContext"))
            }
        }
    }

    /**
     * 构建图片
     */
    fun build(): Image {
        modifier?.modifier(element = layout)

        runTime(message = "布局计算") {
            layout.updateSize()
            layout.updateElementInfo()
        }

        return if (enableOpenGL){
            setWindowSize(layout.size)
            makeGL {
                val renderTarget = BackendRenderTarget.makeGL(
                    layout.size.width.roundToInt(),
                    layout.size.height.roundToInt(),
                    sampleCnt,
                    stencilBits,
                    fbId,
                    fbFormat
                )
                val surface = Surface.makeFromBackendRenderTarget(
                    this,
                    renderTarget,
                    origin,
                    colorFormat,
                    colorSpace
                ) ?: error("Unable to initialize Surface from makeFromBackendRenderTarget")

                runTime("GPU绘制") {
                    layout.draw(ElementDrawContext(surface))
                }

                Image.makeFromBitmap(Bitmap.makeFromImage(surface.makeImageSnapshot(), this))
            }
        }else {
            val surface = Surface.makeRaster(ImageInfo(colorInfo, layout.size.width.roundToInt(), layout.size.height.roundToInt()))
            runTime("绘制") {
                layout.draw(ElementDrawContext(surface))
            }
            surface.makeImageSnapshot()
        }
    }
}

/**
 * 构建图片
 */
fun buildImage(
    modifier: Modifier<Layout>? = null,
    layout: Layout = ColumLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    enableOpenGL: Boolean = true,
    block: Layout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo, enableOpenGL)
    builder.layout.block()
    return builder.build()
}

fun buildImageColumLayout(
    modifier: Modifier<ColumLayout>? = null,
    layout: ColumLayout = ColumLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    enableOpenGL: Boolean = true,
    block: ColumLayout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo, enableOpenGL)
    builder.layout.block()
    return builder.build()
}

fun buildImageRowLayout(
    modifier: Modifier<RowLayout>? = null,
    layout: RowLayout = RowLayout(), // 默认布局
    colorInfo: ColorInfo = ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null),
    enableOpenGL: Boolean = true,
    block: RowLayout.() -> Unit
): Image {
    val builder = ImageBuilder(layout, modifier, colorInfo, enableOpenGL)
    builder.layout.block()
    return builder.build()
}
