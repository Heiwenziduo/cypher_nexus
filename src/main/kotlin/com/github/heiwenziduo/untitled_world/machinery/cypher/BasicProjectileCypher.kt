package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.init.mod.CypherCategoryRegistry

abstract class BasicProjectileCypher(
    override val manaDrain: Float
) : AbstractCypher(), IConsumerCypher {
    init {

//        println("====BasicProjectileCypher====")
//        CypherAttributeRegistry.REGISTRY.holders().forEach { h -> println(h.value()) } // empty when cyphers init
    }
    override val category = CypherCategoryRegistry.PROJECTILE
}