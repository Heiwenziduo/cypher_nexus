package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
data class CypherModifierHelper(
    var MANA_CURRENT: Float,
    var INDEX_CURRENT: Int = 0,
    var DRAW: Int = 1,
    val CYPHER_LIST: List<ResourceLocation>
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

    /**  */
    fun getComputedMap(): HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>> {
        val map = HashMap<Holder<CypherAttribute>,  HashMap<CypherAttributeOperation, Double>>()
        ATTRIBUTE_MAP.forEach { (key, value) -> map[key] = value.getComputedMap() }
        return map
    }

    /***/
    fun call(cypher: AbstractCypher, level: Level, living: LivingEntity, stack: ItemStack) {
        cypher.cast(level, living, stack, this)
        if (cypher is IProviderCypher) {

        }
        if (cypher is IConsumerCypher) {

        }

        if (!level.isClientSide) cypher.onCastServer(level, living, stack, this)
    }
}