package com.github.heiwenziduo.untitled_world.machinery.cypher.attribute

enum class CypherAttributeOperation {
    /**
     * Base value of one cast, mostly on ProjectileCyphers,
     * without base value, the attribute will be ignored.
     * Can be set via special ModifierCyphers.
     * */
    BASE {
        override fun cumulate(last: Double, new: Double): Double {
            TODO("Not yet implemented")
        }
    },
    /** 1.0 -> add 1.0 */
    ADD {
        override fun cumulate(last: Double, new: Double): Double = last + new
    },
    /** 0.33 -> plus 33% */
    MULTIPLY_BASE {
        override fun cumulate(last: Double, new: Double): Double = last + new
    },
    /** 0.33 -> times 33% */
    MULTIPLY_TOTAL {
        override fun cumulate(last: Double, new: Double): Double = last * new
    },
    /**
     * Force an attribute to become an invariable value,
     * will ignore other operations.
     * */
    SET {
        override fun cumulate(last: Double, new: Double): Double = new
    },

    ;
//    abstract fun <T> apply(v1: T, v2: T) : T
    abstract fun cumulate(last: Double, new: Double) : Double
}