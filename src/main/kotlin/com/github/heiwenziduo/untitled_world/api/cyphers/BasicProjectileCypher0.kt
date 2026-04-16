package com.github.heiwenziduo.untitled_world.api.cyphers

import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import com.github.heiwenziduo.untitled_world.api.registries.CypherAttributeRegistry as Attrs

open class BasicProjectileCypher0(

) : AbstractCypher() {
    init {
        addProperty(Attrs.DAMAGE)
        addProperty(Attrs.SPEED)
        addProperty(Attrs.SPREAD)
        addProperty(Attrs.RECOIL)
        addProperty(Attrs.RADIUS)
        addProperty(Attrs.BOUNCE)
        addProperty(Attrs.CRIT_CHANCE)
    }

    override fun cast(
        level: Level,
        player: Player,
        stack: ItemStack,
        helper: CypherModifierHelper
    ) {

    }

}