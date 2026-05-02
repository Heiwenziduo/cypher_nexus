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
    override val resource: ResourceLocation = CypherNexus.modResource("homing")

    init {
        // test data
        addAttribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 1.0)
        addAttribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.5)
        addAttribute(CypherAttributes.EXISTING, CypherAttributeOperation.ADD, 60.0)
        addAttribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 3.0)
        addAttribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 5.0)
    }

    override fun onProjectileTick() {
        TODO("Not yet implemented")
    }
}