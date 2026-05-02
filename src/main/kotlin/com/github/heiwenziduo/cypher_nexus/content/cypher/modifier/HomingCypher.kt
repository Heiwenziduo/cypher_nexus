package com.github.heiwenziduo.cypher_nexus.content.cypher.modifier

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.hook.IProjectileTickHook
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.ModifierCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.resources.ResourceLocation

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