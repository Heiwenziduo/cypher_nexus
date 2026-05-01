package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.init.mod.CypherCategoryRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder

abstract class ModifierCypher (
    override val manaDrain: Float
) : AbstractNonProjectileCypher() {
    init {

    }
    override val draw: Int = 1
    override val category: Holder<CypherCategory> = CypherCategoryRegistry.MODIFIER
}