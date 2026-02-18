package me.owdding.customscoreboard.feature.customscoreboard

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.pipeline.TextureTarget
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.AddressMode
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform
import me.owdding.customscoreboard.Main
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiElementRenderState
import net.minecraft.client.renderer.RenderPipelines
import org.joml.Matrix3x2f
import org.joml.Vector2f
import org.joml.Vector4f
import tech.thatgravyboat.skyblockapi.helpers.McClient

object BlurredBackground {

    private val pipeline: RenderPipeline = RenderPipeline.builder()
        .withLocation(Main.id("blurred_background"))
        .withSampler("Sampler0")
        .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
        .withUniform("Projection", UniformType.UNIFORM_BUFFER)
        .withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER)
        .withFragmentShader(Main.id("core/blurred_background"))
        .withVertexShader(Main.id("core/blurred_background"))
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
        .build()

    init {
        RenderPipelines.register(pipeline)
    }

    private var target: RenderTarget? = null
    private var _uniform: RoundedTextureUniform? = null
    private var multiUseError: Boolean = false

    val uniform: RoundedTextureUniform?
        @JvmStatic get() {
            val uniform = this._uniform
            this._uniform = null
            return uniform
        }
    val texture: GpuTexture? @JvmStatic get() = target?.colorTexture
    var setup: TextureSetup = TextureSetup.noTexture()
        @JvmStatic get
        private set

    @JvmStatic
    fun init(width: Int, height: Int) {
        if (target == null) {
            target = TextureTarget(null, width, height, false)
        } else {
            target!!.resize(width, height)
            setup.texure0?.close()
        }

        setup = TextureSetup.singleTexture(
            RenderSystem.getDevice().createTextureView(target!!.colorTexture!!),
            //? if =1.21.11 {
            RenderSystem.getSamplerCache().getSampler(
                AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE,
                FilterMode.NEAREST, FilterMode.NEAREST,
                false,
            ),
            //?}
        )
    }

    fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int, radius: Int) {
        if (this._uniform != null) {
            if (!this.multiUseError) Main.warn("BlurredBackground.render was called multiple times in the same frame!")
            this.multiUseError = true
        } else {
            val scale = McClient.window.guiScale.toFloat()
            val scaledWidth = width * scale
            val scaledHeight = height * scale

            this._uniform = RoundedTextureUniform.of(
                Vector4f(radius.toFloat()),
                Vector2f(scaledWidth, scaledHeight),
                Vector2f(x * scale + scaledWidth / 2f, y * scale + scaledHeight / 2f),
                scale,
            )
            graphics.guiRenderState.submitGuiElement(
                State(Matrix3x2f(graphics.pose()), ScreenRectangle(x, y, width, height), graphics.scissorStack.peek())
            )
        }
    }

    private data class State(
        val pose: Matrix3x2f,
        val bounds: ScreenRectangle,
        val scissor: ScreenRectangle?,
    ) : GuiElementRenderState {

        override fun buildVertices(consumer: VertexConsumer) {
            consumer.addVertexWith2DPose(this.pose, this.bounds.left().toFloat(), this.bounds.top().toFloat())
            consumer.addVertexWith2DPose(this.pose, this.bounds.left().toFloat(), this.bounds.bottom().toFloat())
            consumer.addVertexWith2DPose(this.pose, this.bounds.right().toFloat(), this.bounds.bottom().toFloat())
            consumer.addVertexWith2DPose(this.pose, this.bounds.right().toFloat(), this.bounds.top().toFloat())
        }

        override fun bounds(): ScreenRectangle = this.bounds
        override fun scissorArea(): ScreenRectangle? = this.scissor

        override fun pipeline(): RenderPipeline = pipeline
        override fun textureSetup(): TextureSetup = setup
    }
}
