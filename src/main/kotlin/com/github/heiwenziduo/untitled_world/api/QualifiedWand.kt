package com.github.heiwenziduo.untitled_world.api

/**
 * Any ItemStack implemented the interface here should be able to conduct cyphers
 * */
interface QualifiedWand {
    val MANA_MAX: Int
    val MANA_REGEN: Int
    val CAPACITY: Int
    val CAST: Int
    val CAST_DELAY: Int
    val RECHARGE_TIME: Int
    val SPREAD: Float
}