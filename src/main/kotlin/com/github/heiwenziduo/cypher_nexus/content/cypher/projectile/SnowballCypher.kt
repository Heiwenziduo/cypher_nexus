package com.github.heiwenziduo.cypher_nexus.content.cypher.projectile

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ProjectileCypher
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.Level

object SnowballCypher : ProjectileCypher(
    manaDrain = 10f
) {
    override val resource = CypherNexus.modResource("snowball")

    init {
//        addAttribute(CypherAttributeRegistry.SPEED, 0.8)

        // test
        addAttribute(CypherAttributes.DAMAGE, 1.0)
        addAttribute(CypherAttributes.SPEED, 0.5)
        addAttribute(CypherAttributes.EXISTING, 200.0)
        addAttribute(CypherAttributes.BOUNCE, 5.0)

    }

    override fun visualEffectOnHit(level: Level, projectile: CypherProjectile) {
        val pos = projectile.position()
        // check: ItemParticleOption(ParticleTypes.ITEM, itemstack), and ParticleTypes.ITEM_SNOWBALL
        for (i in 0..7) {
            level.addParticle(ParticleTypes.ITEM_SNOWBALL, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0)
        }
    }
}