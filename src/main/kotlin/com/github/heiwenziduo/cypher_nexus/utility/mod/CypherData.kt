package com.github.heiwenziduo.cypher_nexus.utility.mod

import com.github.heiwenziduo.cypher_nexus.init.mod.ModCyphers
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.category.CypherCategory

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
            ModCyphers.REGISTRY.toList().forEach { cypher ->
                val list = map.getOrPut(cypher.category.value(), { mutableListOf() })
                list.add(cypher)

                // ====== test ========
//                for (i in 0..40) {
//                    list.add(cypher)
//                }
                // ======      ========
            }
            _cypherMap = map
            println("cypher registry map: $_cypherMap")
        }
    }
}