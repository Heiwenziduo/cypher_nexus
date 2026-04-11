package com.github.heiwenziduo.untitled_world.api.cyphers

/**
 *
 * */
abstract class AbstractCypher {
    abstract val MANA_DRAIN: Int
    abstract val CAST_DELAY: Int
    abstract val RECHARGE_TIME: Int

    abstract val SPREAD: Float
    abstract val SPEED: Float
}