package com.github.heiwenziduo.untitled_world.utility

import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.floor


object VectorUtil {
    fun toVec3i(v: Vec3) = Vec3i(v.x.toInt(), v.y.toInt(), v.z.toInt())

    fun getDireFromHit(hitPoint: Vec3?, aabb: AABB): Direction? {
        println("hitPoint $hitPoint \naabb $aabb")
        if (hitPoint == null) return null
        val epsilon = 1e-5
        return when {
            abs(hitPoint.x - aabb.minX) < epsilon -> Direction.WEST
            abs(hitPoint.x - aabb.maxX) < epsilon -> Direction.EAST
            abs(hitPoint.y - aabb.minY) < epsilon -> Direction.DOWN
            abs(hitPoint.y - aabb.maxY) < epsilon -> Direction.UP
            abs(hitPoint.z - aabb.minZ) < epsilon -> Direction.NORTH
            abs(hitPoint.z - aabb.maxZ) < epsilon -> Direction.SOUTH
            else -> null
        }
    }

    fun getHitDirection(start: Vec3, rayVector: Vec3, aabb: AABB): Direction? {
        // 1. Calculate the exact end point of the ray
        val end = start.add(rayVector)

        // 2. Use vanilla AABB math to find the exact intersection point
        // clip() returns an Optional<Vec3>. It will be empty if the ray completely misses the AABB.
        val hitOptional = aabb.clip(start, end)

        if (hitOptional.isEmpty) {
            return null // The ray didn't hit the bounding box
        }

        val hitPoint = hitOptional.get()

        // 3. A tiny margin of error is needed due to floating-point math inaccuracies
        val epsilon = 1e-5

        // 4. Compare the hit point's coordinates to the AABB's boundaries to find the face
        return when {
            abs(hitPoint.x - aabb.minX) < epsilon -> Direction.WEST
            abs(hitPoint.x - aabb.maxX) < epsilon -> Direction.EAST
            abs(hitPoint.y - aabb.minY) < epsilon -> Direction.DOWN
            abs(hitPoint.y - aabb.maxY) < epsilon -> Direction.UP
            abs(hitPoint.z - aabb.minZ) < epsilon -> Direction.NORTH
            abs(hitPoint.z - aabb.maxZ) < epsilon -> Direction.SOUTH
            else -> null
        }
    }
}

