package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

enum class CypherAttributeOperation {
    /**
     * Base value of one cast, mostly on ProjectileCyphers,
     * without base value, the attribute will be ignored.
     * Can be set via special ModifierCyphers.
     * */
    BASE,
    /** 1.0 -> add 1.0 */
    ADD,
    /** 0.33 -> plus 33% */
    MULTIPLY_BASE,
    /** 0.33 -> times 33% */
    MULTIPLY_TOTAL,
    /**
     * Force an attribute to become an invariable value,
     * will ignore other operations.
     * */
    SET,
}