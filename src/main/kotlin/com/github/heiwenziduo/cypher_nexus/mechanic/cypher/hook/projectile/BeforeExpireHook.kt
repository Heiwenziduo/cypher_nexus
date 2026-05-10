package com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.projectile

import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import net.minecraft.world.level.Level

interface BeforeExpireHook {
    fun beforeExpireBoth(level: Level, projectile: CypherProjectile, strength: Int)
}