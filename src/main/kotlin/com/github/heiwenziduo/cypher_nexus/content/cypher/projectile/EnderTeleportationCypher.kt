package com.github.heiwenziduo.cypher_nexus.content.cypher.projectile

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ProjectileCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.projectile.BeforeExpireHook
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.Level

object EnderTeleportationCypher : ProjectileCypher(
    manaDrain = 20f
), BeforeExpireHook {
    override val resource = CypherNexus.modResource("ender_teleportation")
    override fun beforeExpireBoth(
        level: Level,
        projectile: CypherProjectile,
        strength: Int
    ) {
        val pos = projectile.position()
        if (!level.isClientSide) {
            projectile.owner?.teleportTo(pos.x, pos.y, pos.z)
        }
        for (i in 0..7) {
            level.addParticle(ParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
        }
    }

    init {
        addAttribute(CypherAttributes.SPEED, 1.2)
        addAttribute(CypherAttributes.EXISTING, 20.0)
    }
}