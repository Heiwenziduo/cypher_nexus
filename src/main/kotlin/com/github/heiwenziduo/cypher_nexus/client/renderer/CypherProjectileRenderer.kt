package com.github.heiwenziduo.cypher_nexus.client.renderer

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.BlockRenderDispatcher
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class CypherProjectileRenderer(
    context: EntityRendererProvider.Context
) : EntityRenderer<CypherProjectile>(context) {
    private val itemRenderer: ItemRenderer = context.itemRenderer
    private val blockRenderer: BlockRenderDispatcher = context.blockRenderDispatcher

    val stack = ItemStack(Items.SNOWBALL)

    override fun render(
        p_entity: CypherProjectile,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int // 0-255
    ) {
//        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)

        poseStack.pushPose()
        poseStack.scale(.5f, .5f, .5f)
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation())
        itemRenderer
            .renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                p_entity.level(),
                p_entity.id
            )

        poseStack.popPose()
    }


    override fun getTextureLocation(entity: CypherProjectile): ResourceLocation {
        return CypherNexus.modResource("textures/entity/some_texture.png")
    }
}