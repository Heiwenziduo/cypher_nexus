package com.github.heiwenziduo.untitled_world.content.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.cyphers.AbstractCypher

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
data class CypherModifierHelper(
    var MANA_CURRENT: Float,
    var INDEX_CURRENT: Int = 0,
    var DRAW: Int = 1,
    val CYPHER_LIST: List<AbstractCypher>
) {

    // the operation-system
    var DAMAGE = 0.0f

    fun modifierProjectileProperty(property: ProjectileProperties, operation: Operations, value: Number) {
        UntitledWorld.LOGGER.debug("helper add modifier: {}", CYPHER_LIST[INDEX_CURRENT].toString())
        if (value is Float)
            DAMAGE += value
    }

    /**  */
    fun applyPropertyTo() {
        UntitledWorld.LOGGER.debug(
            "\nhelper apply to a projectile: {}\nCurrent property: {}",
            CYPHER_LIST[INDEX_CURRENT].toString(),
            DAMAGE
        )

    }

    enum class ProjectileProperties {
        DAMAGE
    }

    enum class Operations {
        ADD,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL
    }
}
