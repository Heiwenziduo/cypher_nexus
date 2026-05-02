package com.github.heiwenziduo.cypher_nexus.content.item

import com.github.heiwenziduo.cypher_nexus.init.ModDataComponents.WAND_FREQUENT
import com.github.heiwenziduo.cypher_nexus.init.ModDataComponents.WAND_HIGH_PAYLOAD
import com.github.heiwenziduo.cypher_nexus.init.ModDataComponents.WAND_INVARIABLE
import com.github.heiwenziduo.cypher_nexus.init.mod.ModCyphers
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.machinery.wand.IWandLike
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataHighPayload
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataInvariable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Inventory.SLOT_OFFHAND
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import kotlin.math.min

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

    // test
//    val testData: List<AbstractCypher> = listOf( // there should be a method to reach registry items here
//        ModCyphers.HOMING.value(),
//        ModCyphers.HOMING.value(),
//        ModCyphers.SNOWBALL.value()
//    )

    init {

    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)

        tryConduct(level, player, stack)

        return InteractionResultHolder.success(stack)
    }

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        if (level.isClientSide) return
        // Mob do not implement Container, but can access its inventory through the seven EquipmentSlot enum values:
        // MAINHAND, OFFHAND, FEET, LEGS, CHEST, HEAD, and BODY (where BODY is used for horse and dog armor).
        // entity is Mob && entity.getItemBySlot()

        if (entity is Player && (slotId in 0..8 || slotId == SLOT_OFFHAND)) { // nine hotbar slots (indices 0-8).
            wandTick(stack, entity)
        }


    }
    protected fun wandTick(stack: ItemStack, player: Player) {
        val (invariable, highPayload, frequent) = getWandData(stack, player)?: return // kotliiiiin?
        var flag = false
        val (maxMana, manaRegen) = invariable.chunkF
        var (manaCurrent, index, delay, recharge, rechargeTotal) = frequent
        if (manaCurrent < maxMana) {
            manaCurrent = min(manaCurrent + manaRegen, maxMana)
            flag = true
            println("mana regen -> $manaCurrent")
        }
        if (delay > 0) {
            delay--
            flag = true
        }
        if (index == 0 && recharge > 0) {
            recharge--
            flag = true
        }

        if (flag) stack.set(WAND_FREQUENT, WandDataFrequent(manaCurrent, index, delay, recharge, rechargeTotal))
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

            // test
            val testData: List<AbstractCypher> = listOf(
                    ModCyphers.HOMING.value(),
                    ModCyphers.HOMING.value(),
                    ModCyphers.SNOWBALL.value()
                )
            val t = WandDataHighPayload(testData)

            if (invariable != null && highPayload != null && frequent != null)
//                return IWandLike.WandDataBundle(invariable, highPayload, frequent)
                return IWandLike.WandDataBundle(invariable, t, frequent)
        }
        return null
    }

    override fun setWandData(
        stack: ItemStack?,
        invariable: WandDataInvariable?,
        highPayload: WandDataHighPayload?,
        frequent: WandDataFrequent
    ) {
        /* @doc
         * Any component values within the map should be treated as immutable.
         * Always call #set or one of its referring methods discussed below after modifying the value of a data component.
         * */

        //if (invariable != null)
        stack?.set(WAND_FREQUENT, frequent)
    }


}