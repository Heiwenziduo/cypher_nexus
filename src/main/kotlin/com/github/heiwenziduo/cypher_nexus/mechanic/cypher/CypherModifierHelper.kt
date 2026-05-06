package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.IFlaggable
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.HookContainer
import com.github.heiwenziduo.cypher_nexus.mechanic.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.mechanic.wand.data.WandDataInvariable
import com.github.heiwenziduo.cypher_nexus.utility.mod.CypherUtility
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
class CypherModifierHelper(
    val level: Level,
    val caster: LivingEntity?,
    val stack: ItemStack?,

    val wandStats: WandDataInvariable,
    val cypherList: List<AbstractCypher>,
    val helperData: HelperDataBundle,

    val invokePos: Vec3,
    val invokeDire: Vec3 = Vec3.ZERO,
) : IFlaggable {
    val computedOperationMap = HashMap<CypherAttribute, HashMap<CypherAttributeOperation, Double>>()
    val invokeListTmp = mutableListOf<AbstractCypher>()
    var invokeList = listOf<AbstractCypher>()
    private val projCyList = mutableListOf<AbstractProjectileCypher>()
    override var enabledFlags: Int = 0
    // val hooks = HookContainer()

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
        if (c != null) {
            helperData.delay = CypherUtility.attributeCalculator(c, helperData.delay.toDouble()).toInt()
        }
        if (r != null) {
            helperData.recharge = CypherUtility.attributeCalculator(r, helperData.recharge.toDouble()).toInt()
        }

        // cast end
    }

    /***/
    private fun castLoop() {
        var current: AbstractCypher
        while(helperData.draw >= 1 && helperData.index < cypherList.size) {
            current = cypherList[helperData.index]
            println("preInvoke $current \ncurrent mana: ${helperData.manaCurrent}")
            if (helperData.manaCurrent <= current.manaDrain) {
                println("mana not enough, skip. current index: ${helperData.index}")
                helperData.index++
                continue
            }
            helperData.manaCurrent -= current.manaDrain
            helperData.draw--
            helperData.index++
            preInvoke(current)
            if (current is ModifierCypher) invokeListTmp.add(current)
        }
       invokeList = invokeListTmp.toList()
       for (c in projCyList) {
           invoke(c, invokeList)
       }
    }

    /***/
    private fun preInvoke(cypher: AbstractCypher) {
        helperData.draw += cypher.draw
        enableFlag(cypher.flag)
        cypher.addAttribute(this) // computedMap will include both Consumer-attr(BASE) and modifier-attr
        cypher.onCastServer(level, caster, stack, this, wandStats.chunkF.wandLength)

        when(cypher) {
            is AbstractProjectileCypher -> projCyList.add(cypher)
            is AbstractNonProjectileCypher -> ""
        }
    }
    private fun invoke(cypher: AbstractProjectileCypher, invokes: List<AbstractCypher>) {
        // TODO redirect starting position event
        cypher.createProjectile(level, this, caster, stack, invokePos, invokeDire, invokes)
    }



    data class HelperDataBundle (
        var draw: Int,
        var index: Int,
        var delay: Int,
        var recharge: Int,
        var manaCurrent: Float,
    ) {
        constructor(draw: Int, data: WandDataFrequent) : this(draw, data.index, data.delay, data.recharge, data.manaCurrent)
        fun frequentData() = WandDataFrequent(manaCurrent, index, delay, recharge, 0)
    }

}