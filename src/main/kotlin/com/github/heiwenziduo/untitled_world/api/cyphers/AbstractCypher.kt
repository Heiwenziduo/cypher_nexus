package com.github.heiwenziduo.untitled_world.api.cyphers

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 *
 * */
abstract class AbstractCypher(
//     val MANA_DRAIN: Float,
//     val CAST_DELAY: Int,
//     val RECHARGE_TIME: Int,
//     val DRAW: Int,
) {
    abstract val MANA_DRAIN: Float
    abstract val CAST_DELAY: Int
    abstract val RECHARGE_TIME: Int
    abstract val DRAW: Int

    abstract fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper)
}