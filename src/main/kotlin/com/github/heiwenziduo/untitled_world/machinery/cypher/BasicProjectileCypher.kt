package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.init.mod.CypherCategoryRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class BasicProjectileCypher(
    override val manaDrain: Float
) : AbstractCypher(), IConsumerCypher {
    init {

//        println("====BasicProjectileCypher====")
//        CypherAttributeRegistry.REGISTRY.holders().forEach { h -> println(h.value()) } // empty when cyphers init
    }
    override val category = CypherCategoryRegistry.PROJECTILE
}