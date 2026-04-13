package com.github.heiwenziduo.untitled_world.content.cypher.projectile

import com.github.heiwenziduo.untitled_world.api.cyphers.BasicProjectileCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : BasicProjectileCypher(
    MANA_DRAIN = 50f,
    CAST_DELAY = -2,
    RECHARGE_TIME = 0,
    DAMAGE = 0f,
    SPEED = 100f,
    SPREAD = 2f,
    RECOIL = 100,
    RADIUS = 0f,
    CRIT_CHANCE = 0f,
    BOUNCE = 0,
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