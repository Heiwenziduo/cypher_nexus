package com.github.heiwenziduo.untitled_world.utility

// same types should have the same name, can kotlin do things like typescript?
//typealias AttributeMap<T> = HashMap<CypherAttribute, CypherAttributeInstance>

/**
 * provide several functions to manage CypherAttributes
 * */
object Calculator {
    /*
     * inline & reified allow kotlin to "remember" the exact type of generics at runtime
     *
     * */
    inline fun <reified A : Number> example(n1: A, n2: A): A {
        return when (A::class) {
            Float::class -> (n1.toFloat() + n2.toFloat()) as A
            Int::class -> (n1.toInt() + n2.toInt()) as A
            Double::class -> (n1.toDouble() + n2.toDouble()) as A
            Long::class -> (n1.toLong() + n2.toLong()) as A
            else -> throw IllegalArgumentException("Unsupported Number type!")
        }
    }

    /** + */
    inline fun <reified A : Number> sum(list: List<A>): A {
        return when (A::class) {
            Float::class -> {
                // "when" expression auto return the last line inside the matched block.
                list.sumOf { it.toDouble() }.toFloat() as A
            }
            // "it#toInt" a bit like "arg -> arg#toInt"
            Int::class -> list.sumOf { it.toInt() } as A
            Double::class -> list.sumOf { it.toDouble() } as A

            else -> throw IllegalArgumentException("Calculator unsupported Number type!")
        }
    }

    /** x */
    inline fun <reified A : Number> multi(list: List<A>): A {
        return when (A::class) {
            Float::class -> {
                var r = 0.toFloat()
                list.forEach { r += it.toFloat() }
                r as A
            }
            Int::class -> {
                var r = 0
                list.forEach { r += it.toInt() }
                r as A
            }
            Double::class -> {
                var r = 0.toDouble()
                list.forEach { r += it.toDouble() }
                r as A
            }

            else -> throw IllegalArgumentException("Calculator unsupported Number type!")
        }
    }
}