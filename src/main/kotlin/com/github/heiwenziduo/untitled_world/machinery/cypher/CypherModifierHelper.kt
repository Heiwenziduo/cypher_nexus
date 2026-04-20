package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import net.minecraft.core.Holder
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
    private val ATTRIBUTE_MAP = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()

    // the operation-system
    // instance on helper should be a NEW one, I don't want attributes interfering with each other
    fun addAttribute(map: HashMap<Holder<CypherAttribute>, CypherAttributeInstance>) {
        map.forEach{ attribute, instance ->
            addAttribute(attribute, instance)
        }
    }
    fun addAttribute(attribute: Holder<CypherAttribute>, instance: CypherAttributeInstance) {
        if (attribute !in ATTRIBUTE_MAP) {
            ATTRIBUTE_MAP[attribute] = CypherAttributeInstance(attribute)
        }
        ATTRIBUTE_MAP[attribute]!!.combineWith(instance)
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
        cypher.cast(level, player, stack, this)
        if (cypher is IProviderCypher) {

        }
        if (cypher is IConsumerCypher) {

        }

        cypher.onCast(level, player, stack, this)
    }
}