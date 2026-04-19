package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import net.minecraft.resources.ResourceLocation

/** hold for its static members */
open class CypherAttributeInstance <U : Number> (
    val attribute: CypherAttribute<U>,
    var resource: ResourceLocation? = null
) {
    init {
        if (resource == null)
            resource = attribute.resource
    }

    fun withDefault(value: U) : CypherAttributeInstance<U> {
        OPERATION_MAP.put(
            CypherAttributeOperation.BASE,
            mutableListOf(CypherAttributeModifier(attribute, CypherAttributeOperation.BASE, value))
        )
        return this
    }
    fun withDefault() : CypherAttributeInstance<U> {
        return withDefault(0 as U)
    }

    val OPERATION_MAP = HashMap<CypherAttributeOperation, MutableList<CypherAttributeModifier<U>>>()

    fun addModifier(modifier: CypherAttributeModifier<*>) {
        if (this.attribute != modifier.attribute) return
        modifier as CypherAttributeModifier<U>
        val list = OPERATION_MAP.getOrPut(modifier.operator) { -> mutableListOf(modifier)}
        list.add(modifier)
    }

    /** return null if missing base value */
    open fun compute(): U? {
        return null
    }

    /** merge from another Instance of same type */
    fun combineWith(other: CypherAttributeInstance<*>) {
        if (this.attribute != other.attribute) return
        other as CypherAttributeInstance<U>
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







    // ==========================================================================================================
    // it seems that create different classes for different number-types is a better choice
    // rather than dealing with generic glitches, but this is dumb, to repeat same code many times
    open class AttributeInstanceFloat(
        attribute: CypherAttribute<Float>,
        resource: ResourceLocation? = null
    ) : CypherAttributeInstance<Float>(attribute, resource) {
//        override fun compute(): Float? {
//            if (operationMap[CypherAttributeOperation.SET] != null)
//                return operationMap[CypherAttributeOperation.SET]?.first()?.value
//            // assume operationMap[operation] is null or non-empty
//            if (operationMap[CypherAttributeOperation.BASE] == null)
//                return null
//            val base = operationMap[CypherAttributeOperation.BASE]!!.first().value
//            var add = 0f
//            var multi = 1f
//            var multi_total = 1f
//            for (modifier in operationMap[CypherAttributeOperation.ADD].orEmpty()) {
//                add = modifier.value
//            }
//            for (modifier in operationMap[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()) {
//                multi = modifier.value
//            }
//            for (modifier in operationMap[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()) {
//                multi = modifier.value
//            }
//            return (base)
//        }

    }
    open class AttributeInstanceInt(
        attribute: CypherAttribute<Int>,
        resource: ResourceLocation? = null
    ) : CypherAttributeInstance<Int>(attribute, resource)

    open class AttributeInstanceDouble(
        attribute: CypherAttribute<Double>,
        resource: ResourceLocation? = null
    ) : CypherAttributeInstance<Double>(attribute, resource)

}