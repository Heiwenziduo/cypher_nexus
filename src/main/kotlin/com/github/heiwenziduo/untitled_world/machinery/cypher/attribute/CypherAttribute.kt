package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import com.github.heiwenziduo.untitled_world.utility.i.IRegisterable
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

/**
 * a bit like vanilla LivingEntity's Attribute system
 * */
open class CypherAttribute(
    override val resource: ResourceLocation,
    val defaultValue: Double,
    val min: Double,
    val max: Double,
    val sync: Boolean = true,
    val target: AttributeTarget
): IRegisterable {
    val isProjectileAttribute: Boolean
        get() = target == AttributeTarget.PROJECTILE

    init {
        // UntitledWorld.LOGGER.debug("CypherAttribute created: {}", resource.toString())
    }

    /**
     * create an instance of the Attribute. An instance handles values from specific cyphers,
     * the instance will be created for each Attribute each Cypher
     * */
//    open fun instance() : CypherAttributeInstance = CypherAttributeInstance(this)
    /**
     * create an instance with default value
     * */
//    open fun instance(base: Double) : CypherAttributeInstance =
//        CypherAttributeInstance(this).withDefault(base)


    // ==========================================================================================================
    override fun toString(): String {
        return "attribute_${resource.path}"
    }

    /** lang-JSON key: cypher.attribute.{MOD_ID}.{attribute_name} */
    override fun translation(): MutableComponent =
        Component.translatable("cypher.attribute.${resource.namespace}.${resource.path}")


    enum class AttributeTarget {
        CASTING,
        PROJECTILE
    }
}