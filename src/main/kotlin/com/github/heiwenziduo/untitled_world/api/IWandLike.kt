package com.github.heiwenziduo.untitled_world.api

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * Any ItemStack implemented the interface here should be able to conduct cyphers
 * */
interface IWandLike {
    val MANA_MAX: Float
    val MANA_REGEN: Float
    /** draw numbers for manually cast */
    val DRAW: Int
    val CAPACITY: Int
    val CAST_DELAY: Int
    val RECHARGE_TIME: Int

    // TODO: think of a registry
//    val SPEED: Float
    val SPREAD: Float
//    val RECOIL: Int
//    val RADIUS: Float
//    val CRIT_CHANCE: Float

    fun cast(level: Level, player: Player, stack: ItemStack)

    fun castLoop(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper)
}