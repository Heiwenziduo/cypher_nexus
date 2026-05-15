package com.github.heiwenziduo.cypher_nexus.content.cypher.projectile

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ProjectileCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

object SpitCypher : ProjectileCypher(
    manaDrain = 5f
) {
    override val resource = CypherNexus.modResource("spit")

    init {
        addAttribute(CypherAttributes.RECHARGE_TIME, 2.0)

        addAttribute(CypherAttributes.DAMAGE, 1.0)
        addAttribute(CypherAttributes.SPEED, 1.0)
        addAttribute(CypherAttributes.EXISTING, 300.0)
        addAttribute(CypherAttributes.GRAVITY_FACTOR, 0.06)
        addAttribute(CypherAttributes.CRIT_CHANCE, CypherAttributeOperation.ADD,0.05)
    }

    override fun visualEffectOnHit(level: Level, projectile: CypherProjectile) {
        val pos = projectile.position()

    }
}