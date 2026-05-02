package com.github.heiwenziduo.cypher_nexus.machinery.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataInvariable
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.max
import kotlin.math.min

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
class CypherModifierHelper(
    val level: Level,
    val caster: LivingEntity,
    val stack: ItemStack?,

    val wandStats: WandDataInvariable,
    val cypherList: List<AbstractCypher>,
    val helperData: HelperDataBundle
) {
    val computedOperationMap = HashMap<CypherAttribute, HashMap<CypherAttributeOperation, Double>>()
    val invokeList = mutableListOf<AbstractCypher>()
    var flags: Int = 0

    // the operation-system
    fun addAttribute(map: HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>>) {
        map.forEach{ holder, opMap ->
            opMap.forEach { o, d -> addAttribute(holder.value(), o, d) }
        }
    }
    fun addAttribute(attribute: CypherAttribute, operator: CypherAttributeOperation, value: Double) {
        val operMap = computedOperationMap.getOrPut(attribute) { HashMap() }
        operMap.compute(operator) { k, v -> operator.cumulate(v?: operator.defaultValue, value) }
    }

//    var wandLength = 0f
//    var manaCurrent = 0f
//    var draw = 0
//    var index = 0
//    var delayH = 0
//    var rechargeH = 0
//    var manaCurrent = 0f

    /** test. print map infos */
    fun printComputedMap() {
        val map = computedOperationMap
        CypherNexus.LOGGER.debug("ComputedMapPeek-Helper")
        map.forEach { a, m ->
            println("attribute-${a.registryName()}")
            m.forEach { o, v ->
                println("${o.name}: $v")
            }
        }
    }


    // =========================================================================================================
    /** on server only */
    fun start() {
        if (level.isClientSide) return
        // do some initialization

        // TODO onCastStartEvent
        castLoop()

        helperData.manaCurrent = max(min(helperData.manaCurrent, wandStats.chunkF.maxMana), 0f)
        helperData.index = helperData.index % cypherList.size
        val c = computedOperationMap[CypherAttributes.CAST_DELAY.value()]
        val r = computedOperationMap[CypherAttributes.RECHARGE_TIME.value()]
        if (c != null) {  // maybe I can put these attr calculation into some util...
            val a = c.getOrDefault(CypherAttributeOperation.ADD, CypherAttributeOperation.ADD.defaultValue).toInt()
            val m1 = c.getOrDefault(CypherAttributeOperation.MULTIPLY_BASE, CypherAttributeOperation.MULTIPLY_BASE.defaultValue)
            val m2 = c.getOrDefault(CypherAttributeOperation.MULTIPLY_TOTAL, CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue)
            val s = c[CypherAttributeOperation.SET]?.toInt()
            helperData.delay = s ?: ((helperData.delay + a) * m1 * m2).toInt()
        }
        if (r != null) {
            val a = r.getOrDefault(CypherAttributeOperation.ADD, CypherAttributeOperation.ADD.defaultValue).toInt()
            val m1 = r.getOrDefault(CypherAttributeOperation.MULTIPLY_BASE, CypherAttributeOperation.MULTIPLY_BASE.defaultValue)
            val m2 = r.getOrDefault(CypherAttributeOperation.MULTIPLY_TOTAL, CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue)
            val s = r[CypherAttributeOperation.SET]?.toInt()
            helperData.recharge = s ?: ((helperData.recharge + a) * m1 * m2).toInt()
        }

        // cast end
    }

    /***/
    private fun castLoop() {
        if (helperData.draw <=0 || helperData.index >= cypherList.size) return

        val cypher = cypherList[helperData.index]

        run mana@ {
            println("casting $cypher \ncurrent mana: ${helperData.manaCurrent}")
            if (helperData.manaCurrent <= cypher.manaDrain) {
                println("mana not enough, skip. current index: ${helperData.index}")
                return@mana
            }
            helperData.manaCurrent -= cypher.manaDrain
            invoke(cypher)
            invokeList.add(cypher)
            helperData.draw--
        }

        helperData.index++
        castLoop()

        return
    }

    /***/
    private fun invoke(cypher: AbstractCypher) {
        helperData.draw += cypher.draw
        flags = flags or cypher.flag
        cypher.addAttribute(this) // computedMap will include both Consumer-attr(BASE) and modifier-attr

        when(cypher) {
            is AbstractProjectileCypher -> ""
            is AbstractNonProjectileCypher -> ""
        }

        if (!level.isClientSide)
            cypher.onCastServer(level, caster, stack, this, wandStats.chunkF.wandLength)
    }



    data class HelperDataBundle (
        var draw: Int,
        var index: Int,
        var delay: Int,
        var recharge: Int,
        var manaCurrent: Float,
    ) {
        constructor(draw: Int, data: WandDataFrequent) : this(draw, data.index, data.delay, data.recharge, data.manaCurrent)
        fun createData() = WandDataFrequent(manaCurrent, index, delay, recharge, 0)
    }

}