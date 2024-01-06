package org.sereinfish.catcat.image.skiko.tools.draw.filter

import org.sereinfish.catcat.image.skiko.tools.element.measure.size.FloatSize
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

private val makeEdgeExtensionShaderBuilder = RuntimeShaderBuilder(RuntimeEffect.makeForShader("""
        uniform shader content;
        uniform float width;
        uniform float height;
        
        vec4 main(vec2 coord) {
            float x = clamp(coord.x, 1.0, width - 1.0);
            float y = clamp(coord.y, 1.0, height - 1.0);
            return content.eval(vec2(x, y));
        }
    """.trimIndent()))

/**
 * 边缘扩展滤镜
 */
fun ImageFilter.Companion.makeEdgeExtension(size: FloatSize): ImageFilter {
    makeEdgeExtensionShaderBuilder.uniform("width", size.width)
    makeEdgeExtensionShaderBuilder.uniform("height", size.height)

    return makeRuntimeShader(makeEdgeExtensionShaderBuilder, "content", null)
}