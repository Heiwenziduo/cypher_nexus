package com.github.heiwenziduo.untitled_world.machinery.wand

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.ModDataComponents
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.DAMAGE_BOOST_CYPHER
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.SNOWBALL_CYPHER
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * ---casting logics here---
 * Any Item implemented the interface here should be able to conduct the power of cyphers
 * */
interface IWandLike {
    val infiniteMana: Boolean
        get() = true

    fun validateData(stack: ItemStack): Boolean {
        val wandData = stack.get(ModDataComponents.WAND_DATA)
        val castData = stack.get(ModDataComponents.CAST_DATA)
        if (wandData == null || castData == null) {
            UntitledWorld.LOGGER.warn("wand not valid, missing ${if (wandData == null) "wand" else "cast"}-data! $stack")
        }
        return wandData != null && castData != null
    }

    fun parseCypherList(cyphers: String): List<AbstractCypher> =
        cyphers.split(",").map {
            val str = it.split(".")
            val resource = ResourceLocation.fromNamespaceAndPath(str[0], str[1])
            ModCyphers.getCypherOrThrow(resource)
        }


    /**
     * on manual cast
     * */
    fun cast(level: Level, living: LivingEntity, stack: ItemStack) {
        // read cypher-list from stack
        val cypherList: List<ResourceLocation> = listOf(
            DAMAGE_BOOST_CYPHER.value().resource,
            DAMAGE_BOOST_CYPHER.value().resource,
            SNOWBALL_CYPHER.value().resource
        )
        UntitledWorld.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
//        UntitledWorld.LOGGER.debug("read from data component: {}", stack.get(ModDataComponents.WAND_DATA))

        if (level.isClientSide)
        // send casting info to server
            return

        // read things from stack
        val wandData = stack.get(ModDataComponents.WAND_DATA)
        val castData = stack.get(ModDataComponents.CAST_DATA)
        if (wandData == null || castData == null) {
            UntitledWorld.LOGGER.warn("${living.name} try cast cyphers but missing ${if (wandData == null) "wand" else "cast"} data! $stack")
            return
        }

        var (index, manaCurrent) = castData
        val (manaMax, _, _, draw, wandLength, cyphers) = wandData

        // ... handle "always cast" things
        val helper = CypherModifierHelper(
            manaCurrent = if (infiniteMana) 3.0E9f else manaCurrent,
            manaMax = manaMax,
            index = index,
            draw = draw,
            wandLength = wandLength,
            cypherList = cypherList,
            level = level,
            caster = living,
            stack = stack,
        )
        val (newMana, newIndex) = helper.start()

        // retrieve data from helper and write to components

        // update stack
        /* @doc
         * Any component values within the map should be treated as immutable.
         * Always call #set or one of its referring methods discussed below after modifying the value of a data component.
         * */
        stack.set(ModDataComponents.CAST_DATA, castData.update(newIndex, newMana))

        // auto-sync
        UntitledWorld.LOGGER.debug("server write to data component: {}", stack.get(ModDataComponents.WAND_DATA))

        UntitledWorld.LOGGER.debug("Casting finish...")
    }
}