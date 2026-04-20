package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import kotlin.collections.set

/** immutable? */
open class CypherAttributeInstance (
    val attribute: Holder<CypherAttribute>,
) {
    val resource: ResourceLocation
        get() = attribute.value().resource
    init {
    }
    val OPERATION_MAP = HashMap<CypherAttributeOperation, MutableList<CypherAttributeModifier>>()
    val COMPUTED_MAP = HashMap<CypherAttributeOperation, Double>()
    var isDirty = false

    fun withDefault(value: Double) : CypherAttributeInstance {
        OPERATION_MAP.put(
            // TODO
            CypherAttributeOperation.BASE,
            mutableListOf(CypherAttributeModifier(
                attribute =  attribute,
                operator = CypherAttributeOperation.BASE,
                value =  value,))
        )
        return this
    }
    fun withDefault() : CypherAttributeInstance {
        return withDefault(0.0)
    }


    private fun addModifier(modifier: CypherAttributeModifier) {
        if (this.attribute != modifier.attribute) return
        val list = OPERATION_MAP.getOrPut(modifier.operator) { -> mutableListOf(modifier) }
        list.add(modifier)
        isDirty = true
    }

    private fun computeAndCache() {
        if (!isDirty) return

        val b = OPERATION_MAP[CypherAttributeOperation.BASE].orEmpty()
        val s = OPERATION_MAP[CypherAttributeOperation.SET].orEmpty()
        val a = OPERATION_MAP[CypherAttributeOperation.ADD].orEmpty()
        val mb = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()
        val mt = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_TOTAL].orEmpty()

        val add = a.sumOf { it.value }
        var mul = 1.0
        if (mb.isNotEmpty()) mul = mb.sumOf { it.value }
        var mulTotal = 1.0
        if (mt.isNotEmpty()) {
            mt.forEach { modifier -> mulTotal *= modifier.value }
        }

        COMPUTED_MAP[CypherAttributeOperation.ADD] = add
        COMPUTED_MAP[CypherAttributeOperation.MULTIPLY_BASE] = mul
        COMPUTED_MAP[CypherAttributeOperation.MULTIPLY_TOTAL] = mulTotal

        isDirty = false
        return
    }

    /** return null if missing base value */
    fun compute(): Double? {
        val b = OPERATION_MAP[CypherAttributeOperation.BASE].orEmpty()
        if (b.isEmpty()) return null
        computeAndCache()

        val base = b.first().value
        val a = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.ADD, 0.0)
        val mb = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.MULTIPLY_BASE, 1.0)
        val mt = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.MULTIPLY_TOTAL, 1.0)

        return (base + a) * mb * mt
    }

    /** merge from another Instance of same type */
    fun combineWith(other: CypherAttributeInstance) {
        if (this.attribute != other.attribute) return

        other.OPERATION_MAP.keys.forEach { operation ->
            if (this.OPERATION_MAP[operation].isNullOrEmpty()) {
                // this.operationMap.put(operation, other.operationMap[operation]!!)
                this.OPERATION_MAP += Pair(operation, other.OPERATION_MAP[operation]!!) // kotlin !?
            } else {
                this.OPERATION_MAP[operation]!!.addAll(other.OPERATION_MAP[operation]!!)
            }
        }
    }

//    inline fun <reified A : Number> calculate() : A? {
//        val b = OPERATION_MAP[CypherAttributeOperation.BASE].orEmpty()
//        if (b.isEmpty()) return null
//        val s = OPERATION_MAP[CypherAttributeOperation.SET].orEmpty()
//        if (s.isNotEmpty()) return s.last().value as A
//
//        var base = b.first().value
//        val a = OPERATION_MAP[CypherAttributeOperation.ADD].orEmpty()
//        val mb = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()
//        val mt = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_TOTAL].orEmpty()
////        val add = Calculator.sum(a.map { it.value })
////        val mbase = Calculator.sum(mb.map { it.value })
////        val mtotal = Calculator.multi(mt.map { it.value })
////
////        base = Calculator.sum(listOf(base, add))
////        val result = Calculator.multi(listOf(base, mbase, mtotal))
////
////        return result as A
//        return 0 as A
//    }

    // ==========================================================================================================
    override fun toString(): String {
        return "instanceof $attribute: $OPERATION_MAP"
    }
}