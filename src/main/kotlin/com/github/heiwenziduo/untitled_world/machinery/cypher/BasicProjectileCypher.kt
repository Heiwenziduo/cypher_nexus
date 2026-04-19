package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BasicProjectileCypher(

) : AbstractCypher(), IConsumerCypher {
    init {
//        addAttribute(Attrs.DAMAGE)
//        addAttribute(Attrs.SPEED)
//        addAttribute(Attrs.SPREAD)
//        addAttribute(Attrs.RECOIL)
//        addAttribute(Attrs.RADIUS)
//        addAttribute(Attrs.BOUNCE)
//        addAttribute(Attrs.CRIT_CHANCE)
    }

    override fun cast(
        level: Level,
        player: Player,
        stack: ItemStack,
        helper: CypherModifierHelper
    ) {
        super.cast(level, player, stack, helper)

    }

}