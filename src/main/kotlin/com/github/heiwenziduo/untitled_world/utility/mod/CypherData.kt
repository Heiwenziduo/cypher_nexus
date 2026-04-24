package com.github.heiwenziduo.untitled_world.utility.mod

import com.github.heiwenziduo.untitled_world.init.mod.CypherRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder

// prevent from instantiating
class CypherData private constructor (){
    companion object {
        private var _cypherMap: Map<Holder<CypherCategory>, List<AbstractCypher>> = mapOf()
        val cypherMap
            get() = _cypherMap

        // called during common-setup, can access registry list safely
        // Q: what about other mods modified the registry?
        fun init() {
            val map = mutableMapOf<Holder<CypherCategory>, MutableList<AbstractCypher>>()
            CypherRegistry.REGISTRY.toList().forEach { cypher ->
                val list = map.getOrPut(cypher.category, { mutableListOf() })
                list.add(cypher)
            }
            _cypherMap = map
            println("cypher registry list: $_cypherMap")
        }
    }
}