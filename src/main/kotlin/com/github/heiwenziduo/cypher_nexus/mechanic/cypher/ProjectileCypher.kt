package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.init.mod.CypherCategoryRegistry

abstract class ProjectileCypher(
    override val manaDrain: Float
) : AbstractProjectileCypher() {
    init {

//        println("====BasicProjectileCypher====")
//        CypherAttributeRegistry.REGISTRY.holders().forEach { h -> println(h.value()) } // empty when cyphers init
    }
    override val category = CypherCategoryRegistry.PROJECTILE
}