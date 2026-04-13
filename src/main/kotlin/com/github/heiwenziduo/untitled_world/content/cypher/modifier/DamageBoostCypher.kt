package com.github.heiwenziduo.untitled_world.content.cypher.modifier

import com.github.heiwenziduo.untitled_world.api.cyphers.BasicModifierCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object DamageBoostCypher : BasicModifierCypher(
    MANA_DRAIN = 50f,
    CAST_DELAY = 0,
    RECHARGE_TIME = 0,
    DAMAGE = 1.0f,
    SPEED = 0f,
    SPREAD = 0f,
    RECOIL = 0,
    RADIUS = 0f,
    CRIT_CHANCE = 0f,
    BOUNCE = 0,
) {
    override fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        if (helper.MANA_CURRENT < MANA_DRAIN) return // no mana, then skip
        helper.DRAW--
        helper.MANA_CURRENT -= MANA_DRAIN

        helper.DRAW += this.DRAW

        helper.modifierProjectileProperty(
            CypherModifierHelper.ProjectileProperties.DAMAGE,
            CypherModifierHelper.Operations.ADD,
            DAMAGE
        )
    }
}