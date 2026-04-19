package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

data class CypherAttributeModifier <P : Number> (
    val attribute: CypherAttribute<P>,
    val operator: CypherAttributeOperation,
    val value: P
) {
    // codec-things
}