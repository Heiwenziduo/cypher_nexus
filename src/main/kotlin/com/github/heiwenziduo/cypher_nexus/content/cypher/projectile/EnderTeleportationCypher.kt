package com.github.heiwenziduo.cypher_nexus.content.cypher.projectile

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ProjectileCypher

object EnderTeleportationCypher : ProjectileCypher(
    manaDrain = 20f
) {
    override val resource = CypherNexus.modResource("ender_teleportation")
}