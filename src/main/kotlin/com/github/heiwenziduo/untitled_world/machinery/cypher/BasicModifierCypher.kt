package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BasicModifierCypher private constructor(
    val MANA_DRAIN: Float,
    val CAST_DELAY: Int,
    val RECHARGE_TIME: Int,
    val DRAW: Int,
    val DAMAGE: Float,
    val SPEED: Float,
    val SPREAD: Float,
    val RECOIL: Int,
    val RADIUS: Float,
    val CRIT_CHANCE: Float,
    val BOUNCE: Int,
) : AbstractCypher() {
    constructor(
        MANA_DRAIN: Float,
        CAST_DELAY: Int,
        RECHARGE_TIME: Int,
        DAMAGE: Float,
        SPEED: Float,
        SPREAD: Float,
        RECOIL: Int,
        RADIUS: Float,
        CRIT_CHANCE: Float,
        BOUNCE: Int,
    ) : this(
        MANA_DRAIN,
        CAST_DELAY,
        RECHARGE_TIME,
        1,
        DAMAGE,
        SPEED,
        SPREAD,
        RECOIL,
        RADIUS,
        CRIT_CHANCE,
        BOUNCE,
    ) {

    }
    override fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        TODO("Not yet implemented")
    }
}