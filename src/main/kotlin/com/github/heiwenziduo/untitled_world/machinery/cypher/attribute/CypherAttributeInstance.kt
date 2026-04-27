package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import kotlin.collections.set

/** immutable? */
@Deprecated("")
open class CypherAttributeInstance (
    val attribute: Holder<CypherAttribute>,
) {
    val resource: ResourceLocation
        get() = attribute.value().resource
    init {
    }
    private val OPERATION_MAP = HashMap<CypherAttributeOperation, MutableList<CypherAttributeModifier>>()
    private val COMPUTED_MAP = HashMap<CypherAttributeOperation, Double>()
    var isDirty = true

    fun addModifier(operation: CypherAttributeOperation, value: Double): CypherAttributeInstance {
        val list = OPERATION_MAP.getOrPut(operation) { -> mutableListOf() }
        val modifier = CypherAttributeModifier(
            attribute = attribute,
            operator = operation,
            value = value
        )
        list.add(modifier)
        isDirty = true
        return this
    }

    private fun computeAndCache() {
        // FIXME compute along adding: only add/multiply newly added, or compute after all elements added
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
    fun computeValue(): Double? {
        val b = OPERATION_MAP[CypherAttributeOperation.BASE].orEmpty()
        if (b.isEmpty()) return null
        computeAndCache()

        val base = b.first().value
        val a = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.ADD, 0.0)
        val mb = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.MULTIPLY_BASE, 1.0)
        val mt = COMPUTED_MAP.getOrDefault(CypherAttributeOperation.MULTIPLY_TOTAL, 1.0)

        return (base + a) * mb * mt
    }

    fun getComputedMap(): HashMap<CypherAttributeOperation, Double> {
        computeAndCache()
        return COMPUTED_MAP
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
        isDirty = true
    }


    // ==========================================================================================================
    override fun toString(): String {
        return "\ninstance of ${attribute.value()}: \n$COMPUTED_MAP"
    }
}