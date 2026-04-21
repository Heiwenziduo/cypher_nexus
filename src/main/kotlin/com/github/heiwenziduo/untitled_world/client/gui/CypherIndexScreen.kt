package com.github.heiwenziduo.untitled_world.client.gui

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CypherIndexScreen(title: Component): Screen(title) {
    val indexWidth: Int
        get() = (width * 0.6).toInt()

    override fun init() {
        super.init()
        this.addRenderableWidget(Button.builder(
            UntitledWorld.modTranslation("gui", "cypher_index.button1"),
            { p -> UntitledWorld.LOGGER.debug("button1 clicked  {}", p) }
            ).bounds(this.width / 2 + 5, this.height - 52, 150, 20).build())
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.fill(0, 0, this.width, this.height, 0x333333)
        guiGraphics.fill(0, 0, indexWidth, this.height, 0x333333)


        drawCypher(guiGraphics, 5, 5)

//        RenderSystem.enableBlend()
//        RenderSystem.defaultBlendFunc()
    }

    override fun isPauseScreen() = false

    companion object {
        // fired when player calls the screen

    }

    fun drawCypher(guiGraphics: GuiGraphics, x: Int, y: Int) {
        guiGraphics.blit(SnowballCypher.texture(), x, y, 0, 0, 32, 32)
    }
}