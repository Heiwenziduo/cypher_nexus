package com.github.heiwenziduo.untitled_world.content.item

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.IWandLike
import com.github.heiwenziduo.untitled_world.api.cyphers.AbstractCypher
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.DamageBoostCypher
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.untitled_world.init.ModItems
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
    override val MANA_MAX: Float,
    override val MANA_REGEN: Float,
    override val CAPACITY: Int,
    override val DRAW: Int,
    override val CAST_DELAY: Int,
    override val RECHARGE_TIME: Int,
    override val SPREAD: Float
) : Item(
    Properties()
        .stacksTo(1)
), IWandLike {
    init {

    }

    // item is singleton, cypher data should be put in itemstack
    var INDEX = 0
    val cypherList: List<AbstractCypher> = listOf(SnowballCypher)

    /**
     * on manually cast
     * */
    override fun cast(level: Level, player: Player, stack: ItemStack) {
        if (level.isClientSide) return
        UntitledWorld.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
        // read things from stack
        val _index = 0
        val _mana = 200f
        // ... handle "always cast" things
        val helper = CypherModifierHelper(MANA_CURRENT = _mana, INDEX_CURRENT = _index, CYPHER_LIST = cypherList)
        castLoop(level, player, stack, helper)
        // update stack

        UntitledWorld.LOGGER.debug("Casting finish...")
    }
    override fun castLoop(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        val cypherList = cypherList // read list from stack
        UntitledWorld.LOGGER.info("\nindex: ${helper.INDEX_CURRENT}\nmana: ${helper.MANA_CURRENT}")
        cypherList[helper.INDEX_CURRENT].cast(level, player, stack, helper)
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

    companion object {
        fun testWand(): BasicWandItem {
            return object : BasicWandItem(
                1000f,
                5f,
                10,
                40,
                6,
                1,
                5.0f
            ) {}
        }
    }
}