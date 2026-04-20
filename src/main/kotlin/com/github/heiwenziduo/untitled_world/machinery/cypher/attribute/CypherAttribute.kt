package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.resources.ResourceLocation

/**
 * a bit like vanilla LivingEntity's Attribute system
 * */
open class CypherAttribute(
    val resource: ResourceLocation
) {
    init {
        UntitledWorld.LOGGER.debug("CypherAttribute created: {}", resource.toString())
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

}