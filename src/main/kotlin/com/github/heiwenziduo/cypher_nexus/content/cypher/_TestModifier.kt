package com.github.heiwenziduo.cypher_nexus.content.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.ModifierCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.flag.CypherFlags

object _TestModifier: ModifierCypher(
    manaDrain = 60f
) {
    override val resource = CypherNexus.modResource("test_modifier")

    init {
//         addFlag(CypherFlags.PIERCE_BLOCK)
//         addFlag(CypherFlags.NO_DAMAGE)

        addAttribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 1.0)
        addAttribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 3.0)
        addAttribute(CypherAttributes.EXISTING, CypherAttributeOperation.ADD, 60.0)
        addAttribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 3.0)
        addAttribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 5.0)
    }
}