package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.resources.ResourceLocation

/**
 * a bit like vanilla LivingEntity's Attribute system
 * */
open class CypherAttribute<T : Number>(
    val resource: ResourceLocation
) {
    init {
        UntitledWorld.LOGGER.debug("CypherAttribute created: {}", resource.toString())
    }

    /**
     * create an instance of the Attribute. An instance handles values from specific cyphers,
     * the instance will be created for each Attribute each Cypher
     * */
    open fun instance() : CypherAttributeInstance<T> = CypherAttributeInstance(this)
    /**
     * create an instance with default value
     * */
    open fun instance(base: T) : CypherAttributeInstance<T> =
        CypherAttributeInstance(this).withDefault(base)


    // ==========================================================================================================
    override fun toString(): String {
        return "attribute_${resource.path}"
    }














//    TODO: maybe there is a solution that not have to repeat same code many times?
//    fun instanceWith(type) = AttributeInstanceFloat(this)

    // ============================================================================================================
    open class CypherAttributeInt(resource: ResourceLocation) : CypherAttribute<Int>(resource) {
        override fun instance(): CypherAttributeInstance<Int> = CypherAttributeInstance.AttributeInstanceInt(this)
        override fun instance(base: Int): CypherAttributeInstance<Int> =
            CypherAttributeInstance.AttributeInstanceInt(this).withDefault(base)
    }
    open class CypherAttributeFloat(resource: ResourceLocation) : CypherAttribute<Float>(resource) {
        override fun instance(): CypherAttributeInstance<Float> = CypherAttributeInstance.AttributeInstanceFloat(this)
        override fun instance(base: Float): CypherAttributeInstance<Float> =
            CypherAttributeInstance.AttributeInstanceFloat(this).withDefault(base)
    }
    open class CypherAttributeDouble(resource: ResourceLocation) : CypherAttribute<Double>(resource) {
        override fun instance(): CypherAttributeInstance<Double> = CypherAttributeInstance.AttributeInstanceDouble(this)
        override fun instance(base: Double): CypherAttributeInstance<Double> =
            CypherAttributeInstance.AttributeInstanceDouble(this).withDefault(base)
    }
}