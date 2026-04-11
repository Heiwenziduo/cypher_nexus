package com.github.heiwenziduo.untitled_world.content.item

import com.github.heiwenziduo.untitled_world.api.QualifiedWand
import net.minecraft.world.item.Item

/**
 *
 * */
open class BasicWandItem(
    override val MANA_MAX: Int,
    override val MANA_REGEN: Int,
    override val CAPACITY: Int,
    override val CAST: Int,
    override val CAST_DELAY: Int,
    override val RECHARGE_TIME: Int,
    override val SPREAD: Float
) : Item(
    Properties()
        .stacksTo(1)
), QualifiedWand {
    init {

    }
    companion object {
        fun testWand(): BasicWandItem {
            return object : BasicWandItem(
                1000,
                5,
                10,
                40,
                6,
                1,
                5.0f
            ) {}
        }
    }
}