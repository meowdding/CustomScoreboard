package me.owdding.customscoreboard.feature.customscoreboard

//? >= 26.2 {
//?}
import com.mojang.blaze3d.GpuFormat
import com.mojang.blaze3d.PrimitiveTopology
import com.mojang.blaze3d.pipeline.BindGroupLayout
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
import earth.terrarium.olympus.client.pipelines.uniforms.RoundedTextureUniform
import me.owdding.customscoreboard.Main
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.BindGroupLayouts
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.state.gui.GuiElementRenderState
import org.joml.Matrix3x2f
import org.joml.Vector2f
import org.joml.Vector4f
import tech.thatgravyboat.skyblockapi.helpers.McClient

object BlurredBackground {

    private val pipeline: RenderPipeline = RenderPipeline.builder()
        .withLocation(Main.id("blurred_background"))
        //? >= 26.2 {
        .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
        .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
        .withBindGroupLayout(BindGroupLayouts.GLOBALS)
        .withBindGroupLayout(BindGroupLayouts.PROJECTION)
        .withBindGroupLayout(BindGroupLayout.builder().withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER).build())
        .withVertexBinding(0, DefaultVertexFormat.POSITION)
        .withPrimitiveTopology(PrimitiveTopology.QUADS)
        //?} else {
        /*.withSampler("Sampler0")
        .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
        .withUniform("Projection", UniformType.UNIFORM_BUFFER)
        .withUniform(RoundedTextureUniform.NAME, UniformType.UNIFORM_BUFFER)
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)*///?}
        .withFragmentShader(Main.id("core/blurred_background"))
        .withVertexShader(Main.id("core/blurred_background"))
        .build()

    @JvmStatic
    var needsCopy = false

    //? < 26.2
    //val vulkanInstalled = net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("vulkanmod")

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
        //? < 26.2 {
        /*if (vulkanInstalled) {
            Main.warn("Vulkan mod detected, blurred background will not work!")
            return
        }*///?}
        if (target == null) {
            //? >= 26.2 {
            target = TextureTarget(null, width, height, false, GpuFormat.RGBA8_UNORM) // TODO: confirm format
            //?} else
            //target = TextureTarget(null, width, height, false)
        } else {
            target!!.resize(width, height)
            setup.texure0?.close()
        }

        setup = TextureSetup.singleTexture(
            RenderSystem.getDevice().createTextureView(target!!.colorTexture!!),
            RenderSystem.getSamplerCache().getSampler(
                AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE,
                FilterMode.NEAREST, FilterMode.NEAREST,
                false,
            ),
        )
    }

    fun render(graphics: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int, radius: Int) {
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
            this.needsCopy = true
            graphics.guiRenderState.addGuiElement(
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
