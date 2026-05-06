package com.github.heiwenziduo.cypher_nexus.client.cypher.visualizer.modifier

import com.github.heiwenziduo.cypher_nexus.client.cypher.ICypherVisualizer
import com.github.heiwenziduo.cypher_nexus.content.cypher._TestModifier
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.particles.ParticleTypes

object TestVi : ICypherVisualizer {
    override fun cypher(): AbstractCypher = _TestModifier
    override fun render(
        projectile: CypherProjectile,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        itemRenderer: ItemRenderer,
        blockRenderer: BlockRenderDispatcher,
        entityRenderDispatcher: EntityRenderDispatcher
    ) {
        // println(partialTick) // from 0 to 1
        projectile.level().addParticle(
            // ParticleUtils
            ParticleTypes.BUBBLE,
//            projectile.x + projectile.deltaMovement.x * partialTick,
//            projectile.y + projectile.deltaMovement.y * partialTick,
//            projectile.z + projectile.deltaMovement.z * partialTick,
            projectile.x,
            projectile.y,
            projectile.z,
            0.0, 0.0, 0.0
        )
    }
}