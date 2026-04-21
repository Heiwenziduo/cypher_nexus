package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
data class CypherModifierHelper(
    var manaCurrent: Float,
    var index: Int = 0,
    var draw: Int = 1,
    val cypherList: List<ResourceLocation>
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


    /**  */
    fun getComputedMap(): HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>> {
        // TODO cache
        val map = HashMap<Holder<CypherAttribute>,  HashMap<CypherAttributeOperation, Double>>()
        ATTRIBUTE_MAP.forEach { (key, value) -> map[key] = value.getComputedMap() }
        return map
    }
    /** print map infos */
    fun peekComputedMap() {
        val map = getComputedMap()
        UntitledWorld.LOGGER.debug("ComputedMapPeek-Helper")
        map.forEach { h, m ->
            println("attribute-${h.registeredName}")
            m.forEach { o, v ->
                println("${o.name}: $v")
            }
        }
    }

    /***/
    fun call(cypher: AbstractCypher, level: Level, living: LivingEntity, stack: ItemStack) {
        // check mana
        manaCurrent -= cypher.MANA_DRAIN
        println("casting $cypher \ncurrent mana: $manaCurrent")
        if (manaCurrent <= 0) {
            println("mana not enough!!")
            manaCurrent = 0f
            return
        }
        draw += cypher.DRAW

        cypher.cast(level, living, stack, this)
        if (cypher is IProviderCypher) {

        }
        if (cypher is IConsumerCypher) {

        }

        if (!level.isClientSide) cypher.onCastServer(level, living, stack, this)
    }
}