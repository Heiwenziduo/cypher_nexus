package com.github.heiwenziduo.untitled_world.machinery.wand

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.ModDataComponents
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.DAMAGE_BOOST_CYPHER
import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers.SNOWBALL_CYPHER
import com.github.heiwenziduo.untitled_world.machinery.CypherNotFoundException
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.max
import kotlin.math.min

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
        UntitledWorld.LOGGER.debug("read from data component: {}", stack.get(ModDataComponents.WAND_DATA))

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

        val (index, manaCurrent) = castData
        val (manaMax, _, _, draw, wandLength, cyphers) = wandData

        // ... handle "always cast" things
        val helper = CypherModifierHelper(
            manaCurrent = if (infiniteMana) 3.0E9f else manaCurrent,
            index = index,
            draw = draw,
            wandLength = wandLength,
            cypherList = cypherList
        )
        castLoop(level, living, stack, helper, cypherList)
        val newMana = max(min(helper.manaCurrent, manaMax), 0f)
        val newIndex = helper.index % cypherList.size

        // update stack
        /* @doc
         * Any component values within the map should be treated as immutable.
         * Always call #set or one of its referring methods discussed below after modifying the value of a data component.
         * */
        stack.set(ModDataComponents.CAST_DATA, castData.update(newIndex, newMana))

        castEnd()
        // auto-sync
        UntitledWorld.LOGGER.debug("server write to data component: {}", stack.get(ModDataComponents.WAND_DATA))

        UntitledWorld.LOGGER.debug("Casting finish...")
    }
    // TODO maybe move loop to helper
    fun castLoop(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper, list: List<ResourceLocation>) {
        if (helper.draw <=0 || helper.index >= list.size) return

//        UntitledWorld.LOGGER.info("\nindex: ${helper.INDEX_CURRENT}\nmana: ${helper.MANA_CURRENT}")

        val resource = list[helper.index]
        val cypher = ModCyphers.getCypher(resource)
        if (cypher == null)
            throw CypherNotFoundException("missing cypher: ${resource.namespace}-${resource.path}")

        helper.manaCurrent -= cypher.manaDrain
        println("casting $cypher \ncurrent mana: ${helper.manaCurrent}")
        if (helper.manaCurrent <= 0) {
            println("mana not enough!!")
            helper.manaCurrent = 0f
            return
        }

        helper.call(cypher, level, living, stack)
        helper.index++
        helper.draw--
        castLoop(level, living, stack, helper, list)
    }

    fun castEnd() {}
}