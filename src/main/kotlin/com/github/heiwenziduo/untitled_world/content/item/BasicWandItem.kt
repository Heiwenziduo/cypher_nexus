package com.github.heiwenziduo.untitled_world.content.item

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.DamageBoostCypher
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.untitled_world.machinery.wand.IWandLike
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.init.ModDataComponents
import com.github.heiwenziduo.untitled_world.init.mod.CypherRegistry
import com.github.heiwenziduo.untitled_world.machinery.CypherNotFoundException
import com.github.heiwenziduo.untitled_world.machinery.wand.WandDataComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
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
            // FIXME values reset when starting game
            0,
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

    /**
     * on manually cast
     * */
    override fun cast(level: Level, living: LivingEntity, stack: ItemStack) {
        val cypherList: List<ResourceLocation> = listOf(
            DamageBoostCypher.getResource(),
            DamageBoostCypher.getResource(),
            SnowballCypher.getResource()
        )
        UntitledWorld.LOGGER.debug("Casting start, is client side? {}\nCypherList: {}", level.isClientSide, cypherList)
        UntitledWorld.LOGGER.debug("read from data component: {}", stack.get(ModDataComponents.WAND_DATA))

        if (level.isClientSide)
            // send casting info to server
            return

        // read things from stack
        val _index = 0
        val _mana = 200f
        // ... handle "always cast" things
        val helper = CypherModifierHelper(MANA_CURRENT = _mana, INDEX_CURRENT = _index, CYPHER_LIST = cypherList)
        castLoop(level, living, stack, helper, cypherList)
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
    override fun castLoop(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper, list: List<ResourceLocation>) {
        if (helper.DRAW <=0 || helper.INDEX_CURRENT >= list.size) return

        UntitledWorld.LOGGER.info("\nindex: ${helper.INDEX_CURRENT}\nmana: ${helper.MANA_CURRENT}")
        // check mana condition
        val resource = list[helper.INDEX_CURRENT]
        val cypher = CypherRegistry.REGISTRY.get(resource)
        if (cypher == null)
            throw CypherNotFoundException("missing cypher: ${resource.namespace}-${resource.path}")

        helper.call(cypher, level, living, stack)
        helper.INDEX_CURRENT++
        castLoop(level, living, stack, helper, list)
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