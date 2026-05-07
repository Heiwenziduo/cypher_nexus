package com.github.heiwenziduo.cypher_nexus.client.gui

import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.category.CypherCategory
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.ceil
import kotlin.math.max

@OnlyIn(Dist.CLIENT)
class CypherIndexScreen(
    val cypherMap: Map<CypherCategory, List<AbstractCypher>> = mapOf()
): Screen(Component.empty()) {
    companion object {
        // specifications
        const val ICON_TEXTURE = 16
        const val ICON_SIZE = 14
        const val MARGIN = 8 // space between content and border
        const val PADDING = 2 // space between icons
        const val ITEM_SIZE = ICON_SIZE + PADDING
        const val CATEGORY_TITLE_PADDING = 24

        const val SCROLLBAR_WIDTH = 4
    }
    val indexWidth: Int
        get() = (width * 0.5).toInt()

    private val columns: Int
        get() = max(1, (indexWidth - MARGIN) / ITEM_SIZE)

    private val blocks = mutableListOf<CategoryBlock>()
    private var totalHeight = 100

    // ====== scrollbar ===================
    private var scrollOffset = 0.0
    private var isDraggingScrollbar = false
    private val scrollbarX: Int
        get() = indexWidth - SCROLLBAR_WIDTH - 2
    private val scrollbarHeight: Int
        get() = max(20, (this.height.toFloat() / totalHeight.toFloat() * this.height).toInt())
    private val maxScroll: Double // the maximum amount the screen can scroll down
        get() = max(0.0, totalHeight.toDouble() - height)
    // ====================================

    override fun init() {
        // instance will be created each time player open the screen
        // also fire each time player resizes the window
        // println("window resize")
        super.init()
        if (blocks.isEmpty()) {
            cypherMap.keys.withIndex().forEach { (i, category) ->
                blocks.add(CategoryBlock(category, cypherMap.getOrDefault(category, listOf()), i)) }
        }
        totalHeight = blocks.sumOf { block -> block.blockHeight }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
//        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick)
        guiGraphics.fill(0, 0, this.width, this.height, 0x33333333.toInt())
        guiGraphics.fill(0, 0, indexWidth, this.height, 0xCC333333.toInt()) //
        //
        // scissor test prevents rendering outside these bounds // necessary?
//        guiGraphics.enableScissor(0, 0, indexWidth, this.height)
        for (block in blocks) {
            renderCypherGrid(guiGraphics, mouseX, mouseY, block)
        }
//        guiGraphics.disableScissor()

        renderScrollbar(guiGraphics)
    }

    override fun tick() {
        super.tick()
    }

    override fun isPauseScreen() = false

    override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
        // Only scroll if the mouse is hovering over the left panel
        if (mouseX <= indexWidth) {
            val scrollSpeed = ITEM_SIZE.toDouble() // this will scroll one full row at a time

            // scrollY is typically 1.0 (up) or -1.0 (down)
            scrollOffset = Mth.clamp(scrollOffset - scrollY * scrollSpeed, 0.0, maxScroll)
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // button 0 is the left mouse button
        if (button == 0) {
            // Check if the user clicked horizontally within the scrollbar area
            if (maxScroll > 0 && mouseX >= scrollbarX && mouseX <= scrollbarX + SCROLLBAR_WIDTH) {
                isDraggingScrollbar = true
                updateScrollFromMouse(mouseY)
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
        // handle scrollbar
        if (isDraggingScrollbar) {
            updateScrollFromMouse(mouseY)
            return true
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        // handle scrollbar
        if (button == 0 && isDraggingScrollbar) {
            isDraggingScrollbar = false
            return true
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }


    // ===========================================================================================================
    private fun renderCypherGrid(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, block: CategoryBlock) {
        if (!block.show) return
        val reY = blocks.filter { it.index < block.index }.sumOf { it.blockHeight }

        // if (reY > this.height) return // out of border

        // render category title
        guiGraphics.drawString(font, block.title, PADDING, reY, 0xFFFFFFFF.toInt())
        ////////////////////////

        val cols = columns
        for ((index, cypher) in block.list.withIndex()) {
            val col = index % cols
            val row = index / cols

            val x = PADDING + col * ITEM_SIZE
            val y = reY + PADDING + (row * ITEM_SIZE) - scrollOffset.toInt()

            // Optimization: Only render if the icon is actually visible on screen
            if (y + ICON_SIZE > 0 && y < this.height) {
                // Hover detection
                val isHovered = mouseX in x..(x + ICON_SIZE) && mouseY in y..(y + ICON_SIZE) // kooooootlin
                val bgColor = if (isHovered) 0xFF555555.toInt() else 0xFF444444.toInt()
                // Draw a background slot for the Cypher
                guiGraphics.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, bgColor)
                // Draw the actual icon, should fit ICON_SIZE
                guiGraphics.blit(cypher.texture(), x, y,
                    0f, 0f, ICON_SIZE, ICON_SIZE, ICON_TEXTURE, ICON_TEXTURE)
                // Render tooltip if hovered
                if (isHovered) {
                    guiGraphics.renderTooltip(this.font, cypher.translation(), mouseX, mouseY)
                }
            }
        }
    }

    private fun renderScrollbar(guiGraphics: GuiGraphics) {
        if (maxScroll <= 0) return
        val scrollY = (scrollOffset / maxScroll * (this.height - scrollbarHeight)).toInt()
        // Draw track and thumb
        guiGraphics.fill(scrollbarX, 0, scrollbarX + SCROLLBAR_WIDTH, this.height, 0xFF111111.toInt())
        guiGraphics.fill(scrollbarX, scrollY, scrollbarX + SCROLLBAR_WIDTH, scrollY + scrollbarHeight, 0xFFAAAAAA.toInt())
    }

    // Custom helper method to calculate the new offset
    private fun updateScrollFromMouse(mouseY: Double) {
        // Offset by half the thumb height so the mouse grabs the center of the bar
        val halfThumb = scrollbarHeight / 2.0
        val trackHeight = this.height - scrollbarHeight
        // Calculate percentage (0.0 to 1.0) and clamp it
        val scrollPercentage = Mth.clamp((mouseY - halfThumb) / trackHeight, 0.0, 1.0)
        scrollOffset = scrollPercentage * maxScroll
    }




    private inner class CategoryBlock(
        val category: CypherCategory,
        val list: List<AbstractCypher>,
        val index: Int
    ) {
        val title
            get() = category.translation()

        val show
            get() = list.isNotEmpty()

        val blockRows: Int
            get() = ceil(list.size.toDouble() / columns).toInt()

        val blockHeight: Int
            get() = blockRows * ITEM_SIZE + CATEGORY_TITLE_PADDING
    }
}