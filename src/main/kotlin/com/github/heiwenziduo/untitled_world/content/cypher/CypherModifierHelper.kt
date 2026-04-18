package com.github.heiwenziduo.untitled_world.content.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.cyphers.AbstractCypher
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.Calculator
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttributeInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
data class CypherModifierHelper(
    var MANA_CURRENT: Float,
    var INDEX_CURRENT: Int = 0,
    var DRAW: Int = 1,
    val CYPHER_LIST: List<AbstractCypher>
) {
    private val ATTRIBUTE_MAP = HashMap<CypherAttribute<*>, CypherAttributeInstance<*>>()

    // the operation-system
    fun addCypherAttribute(map: HashMap<CypherAttribute<*>, CypherAttributeInstance<*>?>) {
        // instance on helper should be a NEW one, I don't want attributes interfere with each other
        map.forEach{ attribute, instance ->
            if (instance != null) {
                if (attribute !in ATTRIBUTE_MAP) {
                    ATTRIBUTE_MAP[attribute] = attribute.instance()
                }
                ATTRIBUTE_MAP[attribute]!!.combineWith(instance)
            }
        }
    }

    fun calculateAttr() {

    }

//    fun modifierProjectileProperty(property: ProjectileProperties, operation: Operations, value: Number) {
//        UntitledWorld.LOGGER.debug("helper add modifier: {}", CYPHER_LIST[INDEX_CURRENT].toString())
//        if (value is Float)
//            DAMAGE += value
//    }

    /**  */
    fun applyPropertyTo() {
        UntitledWorld.LOGGER.debug(
            "\nhelper apply to a projectile: {}\nCurrent property: ",
            CYPHER_LIST[INDEX_CURRENT].toString(),
        )
    }

    /***/
    fun call(cypher: AbstractCypher, level: Level, player: Player, stack: ItemStack) {
        cypher.onCast(level, player, stack, this)
    }
}
