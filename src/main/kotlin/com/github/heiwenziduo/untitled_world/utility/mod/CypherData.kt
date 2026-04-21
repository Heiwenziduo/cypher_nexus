package com.github.heiwenziduo.untitled_world.utility.mod

import com.github.heiwenziduo.untitled_world.init.mod.CypherRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher

// prevent from instantiating
class CypherData private constructor (){
    companion object {
        var cypherList: List<AbstractCypher> = listOf()


        // called during common-setup, can access registry list safely
        // Q: what about other mods modified the registry?
        fun init() {
            cypherList = CypherRegistry.REGISTRY.toList()
            println("cypher registry list: $cypherList")
        }
    }
}