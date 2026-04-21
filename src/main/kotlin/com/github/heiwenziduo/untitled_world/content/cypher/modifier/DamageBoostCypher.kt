package com.github.heiwenziduo.untitled_world.content.cypher.modifier

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.BasicModifierCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object DamageBoostCypher : BasicModifierCypher(
    MANA_DRAIN = 20f
) {
    init {
        addAttribute(CypherAttributeRegistry.DAMAGE, CypherAttributeOperation.ADD, 1.0)
    }
    override fun getResource() = UntitledWorld.modResource("damage_boost")
}