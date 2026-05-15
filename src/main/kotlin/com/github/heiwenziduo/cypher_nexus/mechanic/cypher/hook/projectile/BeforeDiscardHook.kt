package com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.projectile

import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import net.minecraft.world.level.Level

interface BeforeDiscardHook {
    fun beforeDiscardBoth(level: Level, projectile: CypherProjectile, strength: Int, reason: CypherProjectile.DiscardReason)
}