package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.resources.ResourceLocation

/** dynamically define a modifier, use to handle wand specialty */
class TemporaryModifierCypher : BasicModifierCypher() {
    override fun getResource() = UntitledWorld.modResource("temporary_modifier")
}