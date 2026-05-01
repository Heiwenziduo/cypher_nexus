package com.github.heiwenziduo.cypher_nexus.machinery.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherCategoryRegistry

/**
 * default registered cypher, like blocks:air, any cypher missing a registry name will be replaced with this.
 * */
object EmptyCypher: AbstractNonProjectileCypher() {
    override val category = CypherCategoryRegistry.OTHER
    override val resource = CypherNexus.modResource("empty_cypher")
}