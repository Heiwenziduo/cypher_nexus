package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.max
import kotlin.math.min

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
class CypherModifierHelper(
    var manaCurrent: Float,
    val manaMax: Float,
    var index: Int = 0,
    var draw: Int = 1,
    val wandLength: Float = 1f,

    val cypherList: List<ResourceLocation>,
    val level: Level,
    val caster: LivingEntity,
    val stack: ItemStack,
) {
    operator fun component1() = manaCurrent // for destructuring
    operator fun component2() = index

    private val _attributeMap = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()

    // the operation-system
    // instance on helper should be a NEW one, I don't want attributes interfering with each other
    fun addAttribute(map: HashMap<Holder<CypherAttribute>, CypherAttributeInstance>) {
        // TODO: optimize memory usage
        map.forEach{ attribute, instance ->
            addAttribute(attribute, instance)
        }
    }
    fun addAttribute(attribute: Holder<CypherAttribute>, instance: CypherAttributeInstance) {
        if (attribute !in _attributeMap) {
            _attributeMap[attribute] = CypherAttributeInstance(attribute)
        }
        _attributeMap[attribute]!!.combineWith(instance)
    }


    /**  */
    fun getComputedMap(): HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>> {
        // TODO cache
        val map = HashMap<Holder<CypherAttribute>,  HashMap<CypherAttributeOperation, Double>>()
        _attributeMap.forEach { (key, value) -> map[key] = value.getComputedMap() }
        return map
    }
    /** print map infos */
    fun printComputedMap() {
        val map = getComputedMap()
        UntitledWorld.LOGGER.debug("ComputedMapPeek-Helper")
        map.forEach { h, m ->
            println("attribute-${h.registeredName}")
            m.forEach { o, v ->
                println("${o.name}: $v")
            }
        }
    }


    // =========================================================================================================
    fun start(): CypherModifierHelper {
        // TODO onCastStartEvent
        castLoop()

        manaCurrent = max(min(manaCurrent, manaMax), 0f)
        index = index % cypherList.size
        return this
    }

    /***/
    private fun call(cypher: AbstractCypher) {
        draw += cypher.draw

        cypher.addAttribute(this)
        if (cypher is IProviderCypher) { }
        if (cypher is IConsumerCypher) { }

        if (!level.isClientSide) cypher.onCastServer(level, caster, stack, this, wandLength)
    }

    /***/
    private fun castLoop() {
        if (draw <=0 || index >= cypherList.size) return

        val resource = cypherList[index]
        val cypher = ModCyphers.getCypherOrThrow(resource)

        manaCurrent -= cypher.manaDrain
        println("casting $cypher \ncurrent mana: ${manaCurrent}")
        if (manaCurrent <= 0) {
            println("mana not enough!!")
            manaCurrent = 0f
            return
        }

        call(cypher)
        index++
        draw--
        castLoop()

        return
    }
}