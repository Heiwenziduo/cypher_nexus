package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.init.mod.CypherCategoryRegistry
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

abstract class ProjectileCypher(
    override val manaDrain: Float
) : AbstractProjectileCypher() {
    init {

//        println("====BasicProjectileCypher====")
//        CypherAttributeRegistry.REGISTRY.holders().forEach { h -> println(h.value()) } // empty when cyphers init
    }
    override val category = CypherCategoryRegistry.PROJECTILE

    final override fun createProjectile(
        level: Level,
        helper: CypherModifierHelper,
        invoker: LivingEntity?,
        stack: ItemStack?,
        startPos: Vec3,
        direction: Vec3?,
        invokeList: List<AbstractCypher>
    ) {
        super.createProjectile(level, helper, invoker, stack, startPos, direction, invokeList)
    }
}