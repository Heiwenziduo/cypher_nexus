package com.github.heiwenziduo.cypher_nexus.content.cypher.modifier

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.ModifierCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.invoking.InvokeRedirectPosHook
import com.github.heiwenziduo.cypher_nexus.utility.RayCastUtility
import com.github.heiwenziduo.cypher_nexus.utility.mod.PosDirePair
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

object DaedalusCypher : ModifierCypher(
    manaDrain = 50f
), InvokeRedirectPosHook {
    const val MARGIN = 0.3f
    override val resource = CypherNexus.modResource("daedalus")
    init {
        addAttribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.3)
    }
    override fun redirectPosDireServer(
        level: ServerLevel,
        invoker: LivingEntity?,
        strength: Int,
        pair: PosDirePair,
        index: Int
    ): PosDirePair {
        val height = -8.0 + 16.0 * strength
        val length = 12.0 + 4.0 * strength
        val (start, direction) = pair
        if (invoker != null && direction != Vec3.ZERO) {
            val route = direction.normalize().scale(length)
            // FIXME why use invoker as projectile?
            val hit = RayCastUtility.getProjectileHitResult(start, invoker,
                { e -> e != invoker && e.canBeHitByProjectile() },
                route, level, MARGIN)
            var remote = start.add(route)
            if (hit.type != HitResult.Type.MISS) {
                remote = hit.location
            }
            // TODO should check SPREAD
            val h = Vec3(0.0, 1.0, 0.0).offsetRandom(invoker.random, 0.3f).scale(height)
            val pos = remote.add(h)
            return PosDirePair(pos, pos.vectorTo(hit.location))
        }
        return pair
    }

}