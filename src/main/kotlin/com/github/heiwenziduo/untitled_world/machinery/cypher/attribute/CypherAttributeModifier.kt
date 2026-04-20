package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

data class CypherAttributeModifier (
    val attribute: CypherAttribute,
    val operator: CypherAttributeOperation,
    val value: Double
) {
    // codec-things
}