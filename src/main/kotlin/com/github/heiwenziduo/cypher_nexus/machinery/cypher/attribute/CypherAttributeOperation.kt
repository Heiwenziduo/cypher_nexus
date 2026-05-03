package com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute

enum class CypherAttributeOperation {
    /**
     * Base value of one cast, mostly on ProjectileCyphers,
     * without base value, the attribute will be ignored.
     * Can be set via special ModifierCyphers.
     * */
    BASE {
        override fun cumulate(last: Double, new: Double): Double {
            return new // we can assume if another BASE passed in, this only happens when another ConsumerCypher is called
        }
    },
    /** 1.0 -> add 1.0 */
    ADD {
        override fun cumulate(last: Double, new: Double): Double = last + new
    },
    /** 0.33 -> plus 33% */
    MULTIPLY_BASE {
        // override val defaultValue = 1.0
        // defaultValue may cumulate multiple times while map initialization(at AbsCypher & Helper)
        override fun cumulate(last: Double, new: Double): Double = last + new
    },
    /** 0.33 -> times 33% */
    MULTIPLY_TOTAL {
        override val defaultValue = 1.0
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
    open val defaultValue: Double = 0.0
    abstract fun cumulate(last: Double, new: Double) : Double
}