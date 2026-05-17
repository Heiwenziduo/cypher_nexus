package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

interface ISecondaryInvokeMarker {
    val subDraw: Int
    val ignoreMana: Boolean
        get() = false
}