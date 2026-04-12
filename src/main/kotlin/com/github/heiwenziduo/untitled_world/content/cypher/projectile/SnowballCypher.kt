package com.github.heiwenziduo.untitled_world.content.cypher.projectile

import com.github.heiwenziduo.untitled_world.api.cyphers.BasicProjectileCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : BasicProjectileCypher(
    0.0f,
    50,
    2,
    2,
    0,
    4.0f,
    10.0f
) {
    /**
     *
     * */
    override fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        if (helper.MANA_CURRENT < MANA_DRAIN) return // no mana, then skip
        helper.DRAW--
        helper.MANA_CURRENT -= MANA_DRAIN

        helper.applyPropertyTo()
    }
}