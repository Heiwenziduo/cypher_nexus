package com.github.heiwenziduo.untitled_world.utility.data

import com.github.heiwenziduo.untitled_world.init.mod.CypherRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder

// prevent from instantiating
class CypherData private constructor (){
    companion object {
        private var _cypherMap: Map<CypherCategory, List<AbstractCypher>> = mapOf()
        val cypherMap
            get() = _cypherMap

        // called during common-setup, can access registry list safely
        // Q: what about other mods modified the registry?
        fun init() {
            val map = mutableMapOf<CypherCategory, MutableList<AbstractCypher>>()
            CypherRegistry.REGISTRY.toList().forEach { cypher ->
                val list = map.getOrPut(cypher.category.value(), { mutableListOf() })
                list.add(cypher)

                // ====== test ========
                for (i in 0..100) {
                    list.add(cypher)
                }
                // ======      ========
            }
            _cypherMap = map
            println("cypher registry map: $_cypherMap")
        }
    }
}