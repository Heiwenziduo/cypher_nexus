package com.github.heiwenziduo.untitled_world.client.gui

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CypherIndexScreen(title: Component, val value: Int = 1): Screen(title) {
    init {
        /**
         * Initializer blocks run when the primary constructor executes.
         * A common use case for init blocks is data validation. For example, by calling the require function.
         * @see require
         * */
        require(value > 0)
        // run first
    }

    constructor(title: Component, value: String): this(title, value.toIntOrNull() ?: 2) {
        /**
         * Secondary constructors are useful when you need multiple ways to initialize a class or for Java interoperability.
         * Note all initializer blocks and property initializers run before the body of the secondary constructor
         * */
        // run then, only if secondary constructor is called
    }

    override fun init() {
        super.init()
        this.addRenderableWidget(Button.builder(
            Component.translatable("gui.untitled_world.spell_index.button1"),
            { p -> UntitledWorld.LOGGER.debug("button1 clicked  {}", p) }
            ).bounds(this.width / 2 + 5, this.height - 52, 150, 20).build())
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.fill(0, 0, this.width, this.height, 1)
    }

    override fun isPauseScreen() = false
}