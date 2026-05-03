package com.github.heiwenziduo.cypher_nexus.machinery.cypher.flag

/**
 * Kotlin provides a few operators for bits operation
 *
 * shl(bits) – signed shift left
 *
 * shr(bits) – signed shift right
 *
 * ushr(bits) – unsigned shift right
 *
 * and(bits) – bitwise AND
 *
 * or(bits) – bitwise OR
 *
 * xor(bits) – bitwise XOR
 *
 * inv() – bitwise inversion
 * */

/** a flag is basically a bundle of booleans, all flag-bits are 0 by default */
enum class CypherFlags(override val value: Int): IFlaggable.IFlagEnum {
    HURT_OWNER(1),
    PIERCE_ENTITY(2),
    PIERCE_BLOCK(4),
    NO_DAMAGE(8),


    ;
    init {
        require(value != 0)
        require(value == 1 || value % 2 == 0)
    }

    companion object {
        fun printFlag(flags: Int) {
            print("number $flags have CypherFlag:\n[")
            for (e in CypherFlags.entries) {
                if (flags and e.value > 0) print("${e.name}, ")
            }
            println("]")
        }
    }
}