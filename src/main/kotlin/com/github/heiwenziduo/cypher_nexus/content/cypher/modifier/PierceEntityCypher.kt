package com.github.heiwenziduo.cypher_nexus.content.cypher.modifier

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ModifierCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.CypherFlags

// this can be registered by using SimpleModifier
object PierceEntityCypher: ModifierCypher(
    70f
) {
    override val resource = CypherNexus.modResource("pierce_entity")

    init {
        addFlag(CypherFlags.HURT_OWNER)
        addFlag(CypherFlags.PIERCE_ENTITY)
    }
}