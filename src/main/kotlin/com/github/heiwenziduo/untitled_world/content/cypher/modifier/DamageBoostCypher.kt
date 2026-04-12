package com.github.heiwenziduo.untitled_world.content.cypher.modifier

import com.github.heiwenziduo.untitled_world.api.cyphers.BasicModifierCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object DamageBoostCypher : BasicModifierCypher(
    20,
    2,
    2,
    1,
    4.0f,
    10.0f
) {
    val DAMAGE = 1.0f

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