package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import net.minecraft.resources.ResourceLocation

/** hold for its static members */
open class CypherAttributeInstance (
    val attribute: CypherAttribute,
    var resource: ResourceLocation? = null
) {
    init {
        if (resource == null)
            resource = attribute.resource
    }

    fun withDefault(value: Double) : CypherAttributeInstance {
        OPERATION_MAP.put(
            CypherAttributeOperation.BASE,
            mutableListOf(CypherAttributeModifier(attribute, CypherAttributeOperation.BASE, value))
        )
        return this
    }
    fun withDefault() : CypherAttributeInstance {
        return withDefault(0.0)
    }

    val OPERATION_MAP = HashMap<CypherAttributeOperation, MutableList<CypherAttributeModifier>>()

    fun addModifier(modifier: CypherAttributeModifier) {
        if (this.attribute != modifier.attribute) return
        val list = OPERATION_MAP.getOrPut(modifier.operator) { -> mutableListOf(modifier)}
        list.add(modifier)
    }

    /** return null if missing base value */
    open fun compute(): Double? {
        return null
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

    inline fun <reified A : Number> calculate() : A? {
        val b = OPERATION_MAP[CypherAttributeOperation.BASE].orEmpty()
        if (b.isEmpty()) return null
        val s = OPERATION_MAP[CypherAttributeOperation.SET].orEmpty()
        if (s.isNotEmpty()) return s.last().value as A

        var base = b.first().value
        val a = OPERATION_MAP[CypherAttributeOperation.ADD].orEmpty()
        val mb = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()
        val mt = OPERATION_MAP[CypherAttributeOperation.MULTIPLY_TOTAL].orEmpty()
//        val add = Calculator.sum(a.map { it.value })
//        val mbase = Calculator.sum(mb.map { it.value })
//        val mtotal = Calculator.multi(mt.map { it.value })
//
//        base = Calculator.sum(listOf(base, add))
//        val result = Calculator.multi(listOf(base, mbase, mtotal))
//
//        return result as A
        return 0 as A
    }

    // ==========================================================================================================
    override fun toString(): String {
        return "instanceof $attribute: $OPERATION_MAP"
    }
}