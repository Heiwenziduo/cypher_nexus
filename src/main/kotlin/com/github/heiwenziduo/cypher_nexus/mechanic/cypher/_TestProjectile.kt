package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import net.minecraft.resources.ResourceLocation

object _TestProjectile: ProjectileCypher(
    manaDrain = 5f
) {
    override val resource = CypherNexus.modResource("test_projectile")
    init {
        addAttribute(CypherAttributes.DAMAGE, 1.0)
        addAttribute(CypherAttributes.SPEED, 0.5)
        addAttribute(CypherAttributes.EXISTING, 200.0)
        addAttribute(CypherAttributes.BOUNCE, 5.0)

    }
}