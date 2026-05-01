package com.github.heiwenziduo.untitled_world.content.item

import com.github.heiwenziduo.untitled_world.init.ModDataComponents.WAND_HIGH_PAYLOAD
import com.github.heiwenziduo.untitled_world.init.ModDataComponents.WAND_FREQUENT
import com.github.heiwenziduo.untitled_world.init.ModDataComponents.WAND_INVARIABLE
import com.github.heiwenziduo.untitled_world.machinery.wand.IWandLike
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataHighPayload
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataInvariable
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
        .component(WAND_INVARIABLE, WandDataInvariable.DEFAULT)
        .component(WAND_HIGH_PAYLOAD, WandDataHighPayload.DEFAULT)
        .component(WAND_FREQUENT, WandDataFrequent.DEFAULT)
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

    override fun getWandData(stack: ItemStack?, caster: LivingEntity?): IWandLike.WandDataBundle? {
//        var stack0: ItemStack
//        if (stack == null && living != null) stack0 = living.getItemInHand(InteractionHand.MAIN_HAND)
        if (stack != null) {
            val invariable = stack.get(WAND_INVARIABLE)
            val highPayload = stack.get(WAND_HIGH_PAYLOAD)
            val frequent = stack.get(WAND_FREQUENT)
            if (invariable != null && highPayload != null && frequent != null) return IWandLike.WandDataBundle(invariable, highPayload, frequent)
        }
        return null
    }

    override fun setWandData(
        stack: ItemStack?,
        invariable: WandDataInvariable?,
        highPayload: WandDataHighPayload?,
        frequent: WandDataFrequent
    ) {
        //if (invariable != null)
        stack?.set(WAND_FREQUENT, frequent)
    }


}