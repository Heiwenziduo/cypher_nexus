package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
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

    private val _attributeComputedMap = HashMap<CypherAttribute, HashMap<CypherAttributeOperation, Double>>()
    val computedMap
        get() = _attributeComputedMap
    // store modifiers before the consumer(projectile)
    val modifierList = mutableListOf<AbstractCypher>()

    // the operation-system
    fun addAttribute(map: HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>>) {
        map.forEach{ holder, opMap ->
            opMap.forEach { o, d -> addAttribute(holder.value(), o, d) }
        }
    }
    fun addAttribute(attribute: CypherAttribute, operator: CypherAttributeOperation, value: Double) {
        val operMap = _attributeComputedMap.getOrPut(attribute) { HashMap() }
        operMap.compute(operator) { k, v -> operator.cumulate(v?: operator.defaultValue, value) }
    }

    /** test. print map infos */
    fun printComputedMap() {
        val map = _attributeComputedMap
        UntitledWorld.LOGGER.debug("ComputedMapPeek-Helper")
        map.forEach { a, m ->
            println("attribute-${a.registryName()}")
            m.forEach { o, v ->
                println("${o.name}: $v")
            }
        }
    }


    // =========================================================================================================
    /***/
    fun start(): CypherModifierHelper {
        // TODO onCastStartEvent
        castLoop()

        manaCurrent = max(min(manaCurrent, manaMax), 0f)
        index = index % cypherList.size
        return this
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

    /***/
    private fun call(cypher: AbstractCypher) {
        draw += cypher.draw
        cypher.addAttribute(this) // computedMap will include both Consumer-attr(BASE) and modifier-attr

        if (cypher is IProviderCypher) { }
        if (cypher is IConsumerCypher) { }

        if (!level.isClientSide) cypher.onCastServer(level, caster, stack, this, wandLength)
    }
}