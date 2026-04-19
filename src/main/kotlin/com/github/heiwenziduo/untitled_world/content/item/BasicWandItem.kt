package com.github.heiwenziduo.untitled_world.content.item

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.wand.IWandLike
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.init.ModDataComponents
import com.github.heiwenziduo.untitled_world.machinery.wand.WandDataComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

/**
 *
 * */
open class BasicWandItem(

) : Item(
    Properties()
        .stacksTo(1)
        .component(ModDataComponents.WAND_DATA, WandDataComponent.WandData(
            500f,
            500f,
            1.6f,
            6,
            1
        ))
), IWandLike {
    init {

    }

    // item is singleton, cypher data should be put in itemstack
    var INDEX = 0
    // cypher&attr registry seems not registered yet, reaching from here (item) lead to crash (?)
//    val cypherList: List<AbstractCypher> = listOf(SnowballCypher) // read list from stack
    val cypherList: List<AbstractCypher> = listOf()

    /**
     * on manually cast
     * */
    override fun cast(level: Level, player: Player, stack: ItemStack) {
        UntitledWorld.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
        UntitledWorld.LOGGER.debug("read from data component: {}", stack.get(ModDataComponents.WAND_DATA))

        if (level.isClientSide)
            // send casting info to server
            return


        // read things from stack
        val _index = 0
        val _mana = 200f
        // ... handle "always cast" things
        if (cypherList.isNotEmpty()) {
            val helper = CypherModifierHelper(MANA_CURRENT = _mana, INDEX_CURRENT = _index, CYPHER_LIST = cypherList)
            castLoop(level, player, stack, helper)
        }
        // update stack


        /**
         * Any component values within the map should be treated as immutable.
         * Always call #set or one of its referring methods discussed below after modifying the value of a data component.
         * */
        stack.update(ModDataComponents.WAND_DATA, WandDataComponent.WandData.DEFAULT) {
            it.manaCurrent -= 200
            it
        }

        // auto-sync
        UntitledWorld.LOGGER.debug("server write to data component: {}", stack.get(ModDataComponents.WAND_DATA))

        UntitledWorld.LOGGER.debug("Casting finish...")
    }
    override fun castLoop(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        cypherList
        UntitledWorld.LOGGER.info("\nindex: ${helper.INDEX_CURRENT}\nmana: ${helper.MANA_CURRENT}")

        helper.call(cypherList[helper.INDEX_CURRENT], level, player, stack)
        // cypherList[helper.INDEX_CURRENT].cast(level, player, stack, helper)
        helper.INDEX_CURRENT++
        if (helper.DRAW > 0 && helper.INDEX_CURRENT < cypherList.size)
            castLoop(level, player, stack, helper)
    }


    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)

        cast(level, player, stack)

        return InteractionResultHolder.success(stack)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.BOW
    }
}