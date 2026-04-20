package com.github.heiwenziduo.untitled_world.machinery.wand

import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * Any ItemStack implemented the interface here should be able to conduct cyphers
 * */
interface IWandLike {
//    val MANA_MAX: Float
//    val MANA_REGEN: Float
    /** draw numbers for manually cast */
//    val DRAW: Int
//    val CAPACITY: Int
//    val CAST_DELAY: Int
//    val RECHARGE_TIME: Int


//    val SPEED: Float
//    val SPREAD: Float
//    val RECOIL: Int
//    val RADIUS: Float
//    val CRIT_CHANCE: Float

    fun cast(level: Level, living: LivingEntity, stack: ItemStack)

    fun castLoop(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper, list: List<ResourceLocation>)
}