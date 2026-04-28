package com.github.heiwenziduo.untitled_world.client.renderer

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.entity.BasicCypherProjectileEntity
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
) : EntityRenderer<BasicCypherProjectileEntity>(context) {
    private val itemRenderer: ItemRenderer = context.itemRenderer
    private val blockRenderer: BlockRenderDispatcher = context.blockRenderDispatcher


    override fun render(
        p_entity: BasicCypherProjectileEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int
    ) {
//        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)

        poseStack.pushPose()
        val stack = ItemStack(Items.SNOWBALL)
        itemRenderer
            .renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                240,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                p_entity.level(),
                p_entity.id
            )

        poseStack.popPose()
    }


    override fun getTextureLocation(entity: BasicCypherProjectileEntity): ResourceLocation {
        return UntitledWorld.modResource("textures/entity/some_texture.png")
    }
}