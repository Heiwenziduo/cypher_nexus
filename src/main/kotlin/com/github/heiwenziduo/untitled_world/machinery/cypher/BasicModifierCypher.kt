package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.init.mod.CypherCategoryRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class BasicModifierCypher (
    override val manaDrain: Float
) : AbstractCypher(), IProviderCypher {
    init {

    }
    override val draw: Int = 1
    override val category: Holder<CypherCategory> = CypherCategoryRegistry.MODIFIER
}