package com.github.heiwenziduo.cypher_nexus.utility.mod

import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation

object CypherUtility {
    fun attributeCalculator(opMap: HashMap<CypherAttributeOperation, Double>, base: Double) : Double {
        val a = opMap.getOrDefault(CypherAttributeOperation.ADD, CypherAttributeOperation.ADD.defaultValue)
        val m1 = opMap.getOrDefault(CypherAttributeOperation.MULTIPLY_BASE, CypherAttributeOperation.MULTIPLY_BASE.defaultValue)
        val m2 = opMap.getOrDefault(CypherAttributeOperation.MULTIPLY_TOTAL, CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue)
        val s = opMap[CypherAttributeOperation.SET]
        return s ?: ((base + a) * (m1 + 1) * m2)
    }


//    fun <T> com(a: T, b: T): T
//    where T : Number, T : Comparable<T> {
//        return a + b
//    }
}