package com.github.heiwenziduo.cypher_nexus.machinery.wand

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.ModCyphers
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.CypherModifierHelper.HelperDataBundle
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataHighPayload
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataInvariable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

/**
 * ---casting logics here---
 * Any Item/LivingEntity implemented the interface here should be able to conduct the power of cyphers #tryConduct
 * */
interface IWandLike {

    abstract fun getWandData(stack: ItemStack?, caster: LivingEntity?): WandDataBundle?
    abstract fun setWandData(stack: ItemStack?, invariable: WandDataInvariable?, highPayload: WandDataHighPayload?, frequent: WandDataFrequent)
    fun setWandData(stack: ItemStack?, frequent: WandDataFrequent) = setWandData(stack, null, null, frequent)
    fun setWandData(stack: ItemStack?, bundle: WandDataBundle) = setWandData(stack, bundle.invariable, bundle.highPayload, bundle.frequent)

    abstract fun getInvokePos(level: Level, caster: LivingEntity, wandLength: Float): Vec3
    abstract fun getInvokeDire(level: Level, caster: LivingEntity): Vec3

    fun parseCypherList(cyphers: String): List<AbstractCypher> =
        cyphers.split(",").map {
            val str = it.split(".")
            val resource = ResourceLocation.fromNamespaceAndPath(str[0], str[1])
            ModCyphers.getCypherOrThrow(resource)
        }


    /**
     * try a manual "draw", may not success due to delay/recharge/disabled/noMana/E.D. e.t.c.
     * */
    fun tryConduct(level: Level, caster: LivingEntity, stack: ItemStack?) {
        // TODO let fake-player/machine can cast cyphers
        val wandData = getWandData(stack, caster)
        if (wandData == null) return
        val (invariable, highPayload, frequent) = wandData
        val (cypherList) = highPayload

        if (level.isClientSide){
            // send casting info to server
            return
        }
        CypherNexus.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
        CypherNexus.LOGGER.debug("read from data component: {}\n\n\n", wandData)


        val bundle = HelperDataBundle(invariable.chunkI.draw, frequent)
        val helper = CypherModifierHelper(level, caster, stack, invariable, cypherList, bundle,
            getInvokePos(level, caster, invariable.chunkF.wandLength), getInvokeDire(level, caster))
        helper.start()

        // retrieve data from helper and write to components
        setWandData(stack, bundle.frequentData())

        // auto-sync
        CypherNexus.LOGGER.debug("server write to data component: {}", bundle)

        CypherNexus.LOGGER.debug("Casting finish...")
    }


    data class WandDataBundle(val invariable: WandDataInvariable, val highPayload: WandDataHighPayload, val frequent: WandDataFrequent)
}