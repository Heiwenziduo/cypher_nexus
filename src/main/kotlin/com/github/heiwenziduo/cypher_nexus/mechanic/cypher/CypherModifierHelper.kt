package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherBehaviorHookRegistry
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.IFlaggable
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.HookContainer
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.HookModule
import com.github.heiwenziduo.cypher_nexus.mechanic.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.mechanic.wand.data.WandDataInvariable
import com.github.heiwenziduo.cypher_nexus.utility.mod.CypherUtility
import com.github.heiwenziduo.cypher_nexus.utility.mod.PosDirePair
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.max
import kotlin.math.min

/** a modifier carrier created when user manually cast, or a trigger-cypher is fired */
class CypherModifierHelper(
    val level: Level,
    val invoker: LivingEntity?,
    val stack: ItemStack?,

    val wandStats: WandDataInvariable,
    val cypherList: List<AbstractCypher>,
    val helperData: HelperDataBundle,

    /** direction doesn't have to be normalized */
    val invokePosDire: PosDirePair,
) : IFlaggable {
    companion object {
        const val MAX_DEPTH = 1
    }
    val computedOperationMap = HashMap<CypherAttribute, HashMap<CypherAttributeOperation, Double>>()
    val invokingAttrMap = HashMap<CypherAttribute, Double>()
    val invokeListTmp = mutableListOf<AbstractCypher>()
    var invokeList = listOf<AbstractCypher>()
    private val projCyList = mutableListOf<AbstractProjectileCypher>()
    override var enabledFlags: Int = 0
    val invokeHookContainer = HookContainer(HookModule.HookType.INVOKING)
    private var _recurDepth = 0
    val recurDepth
        get() = _recurDepth
    // the operation-system
    fun addAttribute(map: HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>>) {
        map.forEach{ holder, opMap ->
            // TODO base value is not used here
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
        helperData.delay += wandStats.chunkI.castDelay
        helperData.recharge += wandStats.chunkI.rechargeTime

        // TODO onCastStartEvent
        conductLoop()

        helperData.manaCurrent = max(min(helperData.manaCurrent, wandStats.chunkF.manaMax), 0f)
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
    private fun conductLoop() {
        val startIndex = helperData.index
        var currentDepth = 0
        var current: AbstractCypher
        var i: Int
        while(helperData.draw >= 1) {
            i = helperData.index++
            if (helperData.index >= cypherList.size) {
                helperData.index = helperData.index % cypherList.size
                currentDepth++
                if (currentDepth > MAX_DEPTH) break
            }

            current = cypherList[i]
            if (current is EmptyCypher) continue

            println("preInvoke [$current] \ncurrent mana: ${helperData.manaCurrent}")
            if (helperData.manaCurrent <= current.manaDrain) {
                println("mana not enough, skip. current index: $i")
                continue
            }
            helperData.manaCurrent -= current.manaDrain
            helperData.draw--
            preInvoke(current)
            if (current is AbstractNonProjectileCypher) {
                invokeListTmp.add(current)
            }

            if (currentDepth == MAX_DEPTH && helperData.index >= startIndex) {
                println("----reach max recursive depth----\n")
                break
            }
        }

        // check if there only empty, if so, reset index to 0 (start reload)
        var isFinished = true
        if (currentDepth <= 0) { // if recursive, set to 0 immediately after conducting
            for (i in cypherList.size - 1 downTo helperData.index) {
                val cy = cypherList[i]
                if (cy !is EmptyCypher) {
                    isFinished = false
                    break
                }
            }
        }
        if (isFinished) helperData.index = 0



        // if (projCyList.isEmpty()) return
        val pair = invokeHookContainer.cumulateHooks(CypherBehaviorHookRegistry.INVOKE_REDIRECT_POS, invokePosDire)
        { h, l, i -> h.redirectPosDireServer(level as ServerLevel, invoker, l, i) }

        invokeList = invokeListTmp.toList()
        for (c in projCyList) {
            invokeProjectile(c, invokeList, pair)
        }
    }

    /***/
    private fun preInvoke(cypher: AbstractCypher) {
        helperData.draw += cypher.draw
        cypher.addAttribute(this) // computedMap will include both Consumer-attr(BASE) and modifier-attr
        cypher.onInvokeServer(level, invoker, stack, this, wandStats.chunkF.wandLength)
        invokeHookContainer.add(cypher)

        when(cypher) {
            is AbstractProjectileCypher -> projCyList.add(cypher)
            is AbstractNonProjectileCypher -> enableFlag(cypher.flag)
        }
    }
    private fun invokeProjectile(cypher: AbstractProjectileCypher, invokes: List<AbstractCypher>, posDirePair: PosDirePair) {
        cypher.createProjectile(level, this, invoker, stack, posDirePair, invokes)
    }



    data class HelperDataBundle (
        var draw: Int,
        var index: Int,
        var delay: Int,
        var recharge: Int,
        var manaCurrent: Float,
    ) {

        constructor(draw: Int, data: WandDataFrequent) : this(draw, data.index, data.delay, data.recharge, data.manaCurrent)
        fun frequentData() = WandDataFrequent(manaCurrent, index, delay, recharge,)
    }

}