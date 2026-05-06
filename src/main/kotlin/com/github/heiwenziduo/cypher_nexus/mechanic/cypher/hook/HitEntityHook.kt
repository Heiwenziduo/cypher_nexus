package com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook

import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

interface HitEntityHook {
    fun onHitEntityServer(level: Level, projectile: CypherProjectile, strength: Int, target: Entity)
}