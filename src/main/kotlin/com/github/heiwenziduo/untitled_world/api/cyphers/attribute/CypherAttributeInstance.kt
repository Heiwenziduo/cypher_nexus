package com.github.heiwenziduo.untitled_world.api.cyphers.attribute

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
        operationMap.put(
            CypherAttributeOperation.BASE,
            mutableListOf(CypherAttributeModifier(attribute, CypherAttributeOperation.BASE, value))
        )
        return this
    }

    val operationMap = HashMap<CypherAttributeOperation, MutableList<CypherAttributeModifier<U>>>()

    fun addModifier(modifier: CypherAttributeModifier<U>) {
        val list = operationMap.getOrPut(modifier.operator) { -> mutableListOf(modifier)}
        list.add(modifier)
    }

    /** return null if missing base value */
    open fun compute(): U? {
        return null
    }







    // ==========================================================================================================
    // it seems that create different classes for different number-types is a better choice
    // rather than dealing with generic glitches, but this is dumb, to repeat same code many times
    open class AttributeInstanceFloat(
        attribute: CypherAttribute<Float>,
        resource: ResourceLocation? = null
    ) : CypherAttributeInstance<Float>(attribute, resource) {
        override fun compute(): Float? {
            if (operationMap[CypherAttributeOperation.SET] != null)
                return operationMap[CypherAttributeOperation.SET]?.first()?.value
            // assume operationMap[operation] is null or non-empty
            if (operationMap[CypherAttributeOperation.BASE] == null)
                return null
            val base = operationMap[CypherAttributeOperation.BASE]!!.first().value
            var add = 0f
            var multi = 1f
            var multi_total = 1f
            for (modifier in operationMap[CypherAttributeOperation.ADD].orEmpty()) {
                add = modifier.value
            }
            for (modifier in operationMap[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()) {
                multi = modifier.value
            }
            for (modifier in operationMap[CypherAttributeOperation.MULTIPLY_BASE].orEmpty()) {
                multi = modifier.value
            }
            return (base)
        }

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