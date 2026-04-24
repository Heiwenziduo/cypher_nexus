package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation

data class CypherAttributeModifier (
    val attribute: Holder<CypherAttribute>,
    val operator: CypherAttributeOperation,
    val value: Double,
//    val cypher: Holder<AbstractCypher>
) {
//    val resource: ResourceLocation
//        get() = UntitledWorld.modResource(cypher.registeredName)

    // codec-things
}