package com.github.heiwenziduo.untitled_world.api.cyphers.attribute

/**
 * provide several functions to manage CypherAttributes
 * */
object Calculator {
    // TODO:
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
}