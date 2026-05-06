package com.github.heiwenziduo.cypher_nexus.content.cypher.modifier

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.hook.IProjectileTickHook
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ModifierCypher

object HomingCypher: ModifierCypher(
    manaDrain = 50f
), IProjectileTickHook {
    override val resource = CypherNexus.modResource("homing")

    init {

    }

    override fun onProjectileTick() {
        TODO("Not yet implemented")
    }
}