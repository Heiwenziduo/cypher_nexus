package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherCategoryRegistry

/**
 * default registered cypher, like blocks:air, any cypher missing a registry name will be replaced with this.
 * */
object EmptyCypher: AbstractCypher(), IConsumerCypher {
    override val category = CypherCategoryRegistry.OTHER
    override val resource = UntitledWorld.modResource("empty_cypher")
}