package com.github.heiwenziduo.untitled_world.api.cyphers

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BasicModifierCypher(
    override val MANA_DRAIN: Int,
    override val CAST_DELAY: Int,
    override val RECHARGE_TIME: Int,
    override val DRAW: Int,
    override val SPREAD: Float,
    override val SPEED: Float
) : AbstractCypher() {
    override fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        TODO("Not yet implemented")
    }
}