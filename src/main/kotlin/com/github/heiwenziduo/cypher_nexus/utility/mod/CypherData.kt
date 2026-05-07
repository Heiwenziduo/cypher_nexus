package com.github.heiwenziduo.cypher_nexus.utility.mod

import com.github.heiwenziduo.cypher_nexus.init.mod.ModCyphers
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.category.CypherCategory
import net.minecraft.server.level.ServerPlayer

// prevent from instantiating
class CypherData private constructor (){
    companion object {
        private val _enabledList: List<AbstractCypher> by lazy {
            val list = ModCyphers.REGISTRY.toList().filter { true } // TODO server-config?
            println("cypher _enabledList init: $list")
            list
        }
        private val _categoryMap: Map<CypherCategory, List<AbstractCypher>> by lazy {
            val map = CypherUtility.sortCyphersByCategory(_enabledList)
            println("cypher _categoryMap init: $map")
            map
        }

        val cypherCategoryMap // _cypherMap will not compute (or take memory) if this is not called
            get() = _categoryMap

        val cyphersEnabled // guess these two should not be called on logical client
            get() = _enabledList

        fun cyphersPlayerUnlocked (player: ServerPlayer): List<AbstractCypher> {
            return _enabledList
        }

        // called during common-setup, can access registry list safely
        // Q: what about other mods modified the registry?
        // A: access it during game play only, this timing make sure all registry is settled

//        fun init() {
//            val map = mutableMapOf<CypherCategory, MutableList<AbstractCypher>>()
//            ModCyphers.REGISTRY.toList().forEach { cypher ->
//                val list = map.getOrPut(cypher.category.value(), { mutableListOf() })
//                list.add(cypher)
//
//                // ====== test ========
////                for (i in 0..40) {
////                    list.add(cypher)
////                }
//                // ======      ========
//            }
//            _cypherMap = map
//        }
    }
}