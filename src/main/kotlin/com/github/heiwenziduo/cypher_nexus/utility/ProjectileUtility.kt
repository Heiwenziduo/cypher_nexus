package com.github.heiwenziduo.cypher_nexus.utility

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import java.util.function.Predicate

object ProjectileUtility {
    /**
     * custom projectile hit check function exactly same as {net.minecraft.world.entity.projectile.ProjectileUtil.getHitResult()},
     * but avoid magic number "0.3" (e.g. margin)
     * */
    fun getHitResult(start: Vec3, entity: Entity, filter: Predicate<Entity>, deltaMovement: Vec3, level: Level, margin: Float, clipContext: ClipContext.Block) : HitResult {
        var end = start.add(deltaMovement)
        var hitresult: HitResult = level.clip(ClipContext(start, end, clipContext, ClipContext.Fluid.NONE, entity))
        if (hitresult.type != HitResult.Type.MISS) {
            end = hitresult.getLocation()
        }

        val hitresult1: HitResult? = ProjectileUtil.getEntityHitResult(
            level,
            entity,
            start,
            end,
            entity.boundingBox.expandTowards(deltaMovement).inflate(1.0),
            filter,
            margin
        )
        if (hitresult1 != null) {
            hitresult = hitresult1
        }

        return hitresult
    }
}