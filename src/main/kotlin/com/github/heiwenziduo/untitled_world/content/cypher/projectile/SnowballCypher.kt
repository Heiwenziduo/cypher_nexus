package com.github.heiwenziduo.untitled_world.content.cypher.projectile

import com.github.heiwenziduo.untitled_world.api.cyphers.BasicProjectileCypher0
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttributeModifier
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttributeOperation
import com.github.heiwenziduo.untitled_world.api.registries.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : BasicProjectileCypher0(

) {
    /**
     *
     * */
    override fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        super.cast(level, player, stack, helper)
//        if (helper.MANA_CURRENT < MANA_DRAIN) return // no mana, then skip
//        helper.DRAW--
//        helper.MANA_CURRENT -= MANA_DRAIN
//
//        helper.applyPropertyTo()
    }

    val dataset = listOf(
        CypherAttributeModifier(CypherAttributeRegistry.MANA_DRAIN, CypherAttributeOperation.BASE, 50f),
        CypherAttributeModifier(CypherAttributeRegistry.SPEED, CypherAttributeOperation.BASE, 50f),
        CypherAttributeModifier(CypherAttributeRegistry.DAMAGE, CypherAttributeOperation.BASE, 0.0),
        CypherAttributeModifier(CypherAttributeRegistry.CAST_DELAY, CypherAttributeOperation.ADD, 0),
        CypherAttributeModifier(CypherAttributeRegistry.RECHARGE_TIME, CypherAttributeOperation.ADD, 0),
        CypherAttributeModifier(CypherAttributeRegistry.RECOIL, CypherAttributeOperation.ADD, 2f),
    )
    /**
     * TODO: optimize structure
     * */
    fun readAttributeFromDataList() {
        dataset.forEach { modifier -> ATTRIBUTE_MAP[modifier.attribute] }
    }
}