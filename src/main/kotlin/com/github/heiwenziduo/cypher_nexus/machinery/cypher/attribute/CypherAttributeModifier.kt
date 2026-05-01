package com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute

import net.minecraft.core.Holder

@Deprecated("")
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