package com.github.heiwenziduo.untitled_world.machinery.wand

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.DAMAGE_BOOST
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.SNOWBALL
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataHighPayload
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataInvariable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * ---casting logics here---
 * Any Item implemented the interface here should be able to conduct the power of spells
 * */
interface IWandLike {
    val infiniteMana: Boolean
        get() = true


    abstract fun getWandData(stack: ItemStack?, caster: LivingEntity?): WandDataBundle?
    abstract fun setWandData(stack: ItemStack?, invariable: WandDataInvariable?, highPayload: WandDataHighPayload?, frequent: WandDataFrequent)
    fun setWandData(stack: ItemStack?, frequent: WandDataFrequent) = setWandData(stack, null, null, frequent)
    fun setWandData(stack: ItemStack?, bundle: WandDataBundle) = setWandData(stack, bundle.invariable, bundle.highPayload, bundle.frequent)


    fun parseCypherList(cyphers: String): List<AbstractCypher> =
        cyphers.split(",").map {
            val str = it.split(".")
            val resource = ResourceLocation.fromNamespaceAndPath(str[0], str[1])
            ModCyphers.getCypherOrThrow(resource)
        }


    /**
     * a manual "draw"
     * */
    fun cast(level: Level, caster: LivingEntity, stack: ItemStack?) {
        // TODO let fake-player/machine can cast spells
        val wandData = getWandData(stack, caster)
        if (wandData == null) return
        val (invariable, highPayload, frequent) = wandData
        val (max, regen, length) = invariable.chunk0
        val (capa, draw, delay, recharge) = invariable.chunk1
        val (always) = invariable.chunk2
        val (spellList) = highPayload
        val (manaCurrent, index) = frequent

        // read cypher-list from stack
        // TODO data read from codec is list<AbstractCypher> directly
        val cypherList: List<ResourceLocation> = listOf(
            DAMAGE_BOOST.value().resource,
            DAMAGE_BOOST.value().resource,
            SNOWBALL.value().resource
        )

        UntitledWorld.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
//        UntitledWorld.LOGGER.debug("read from data component: {}", stack.get(ModDataComponents.WAND_DATA))
        if (level.isClientSide)
        // send casting info to server
            return


        // ... handle "always cast" things
        val helper = CypherModifierHelper(
            manaCurrent = if (infiniteMana) 3.0E9f else manaCurrent,
            manaMax = max,
            index = index,
            draw = draw,
            wandLength = length,
            cypherList = cypherList,
            level = level,
            caster = caster,
            stack = stack,
        )
        // retrieve data from helper and write to components
        val (newManaCurrent, newIndex) = helper.start()
        setWandData(stack, WandDataFrequent(newManaCurrent, newIndex))

        /* @doc
         * Any component values within the map should be treated as immutable.
         * Always call #set or one of its referring methods discussed below after modifying the value of a data component.
         * */

        // auto-sync
        UntitledWorld.LOGGER.debug("server write to data component: {}", 1)

        UntitledWorld.LOGGER.debug("Casting finish...")
    }


    data class WandDataBundle(val invariable: WandDataInvariable, val highPayload: WandDataHighPayload, val frequent: WandDataFrequent)
}