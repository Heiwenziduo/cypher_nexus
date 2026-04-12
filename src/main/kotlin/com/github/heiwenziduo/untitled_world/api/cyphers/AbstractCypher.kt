package com.github.heiwenziduo.untitled_world.api.cyphers

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 *
 * */
abstract class AbstractCypher {
    abstract val MANA_DRAIN: Int
    abstract val CAST_DELAY: Int
    abstract val RECHARGE_TIME: Int
    abstract val DRAW: Int

    abstract val SPREAD: Float
    abstract val SPEED: Float

    abstract fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper)
}