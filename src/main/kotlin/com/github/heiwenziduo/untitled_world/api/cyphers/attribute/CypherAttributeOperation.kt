package com.github.heiwenziduo.untitled_world.api.cyphers.attribute

enum class CypherAttributeOperation {
    /**
     * Base value of one cast, mostly on ProjectileCyphers,
     * without base value, the attribute will be ignored.
     * Can be set via special ModifierCyphers.
     * */
    BASE,
    ADD,
    MULTIPLY_BASE,
    MULTIPLY_TOTAL,
    /**
     * Force an attribute to become an invariable value,
     * will ignore other operations.
     * */
    SET,
}