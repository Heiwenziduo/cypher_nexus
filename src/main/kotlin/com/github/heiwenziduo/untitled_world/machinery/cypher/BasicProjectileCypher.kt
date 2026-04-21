package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class BasicProjectileCypher(
    override val MANA_DRAIN: Float
) : AbstractCypher(), IConsumerCypher {
    init {
        addAttribute(CypherAttributeRegistry.RECOIL, 0.0)
        addAttribute(CypherAttributeRegistry.BOUNCE, 0.0)
        addAttribute(CypherAttributeRegistry.CRIT_CHANCE, 0.0)
        // addAttribute(CypherAttributeRegistry.RADIUS, 1.0)
    }
}