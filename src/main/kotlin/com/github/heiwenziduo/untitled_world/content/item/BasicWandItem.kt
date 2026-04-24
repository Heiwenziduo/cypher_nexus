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
import kotlin.math.max
import kotlin.math.min

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

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)

        cast(level, player, stack)

        return InteractionResultHolder.success(stack)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.BOW
    }
}