package com.github.heiwenziduo.cypher_nexus.content.cypher.modifier

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.hook.IProjectileTickHook
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.ModifierCypher
import net.minecraft.resources.ResourceLocation

object HomingCypher: ModifierCypher(
    manaDrain = 50f
), IProjectileTickHook {
    override val resource: ResourceLocation = CypherNexus.modResource("homing")

    override fun onProjectileTick() {
        TODO("Not yet implemented")
    }
}