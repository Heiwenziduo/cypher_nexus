package com.github.heiwenziduo.cypher_nexus.client

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.CypherNexus.LOGGER
import com.github.heiwenziduo.cypher_nexus.client.renderer.CypherProjectileRenderer
import com.github.heiwenziduo.cypher_nexus.init.ModEntities.CYPHER_PROJECTILE
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers

@EventBusSubscriber(modid = CypherNexus.MOD_ID, value = [Dist.CLIENT])
object ClientSetup {
    // TODO: separate logical and physical clients?

    /**
     * This is used for initializing client specific things such as renderers and keymaps.
     * Fired on the mod specific event bus.
     *
     * Sided setup event's dist seems to be auto-detected, "...The sided setup is fired:
     * FMLClientSetupEvent if on a physical client, and FMLDedicatedServerSetupEvent if on a physical server..."
     */
    @SubscribeEvent
    private fun onClientStarting(event: FMLClientSetupEvent) {
        LOGGER.info("HELLO FROM CLIENT SETUP")
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
    }

    // ===================== entity renderer ================================
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(CYPHER_PROJECTILE.get()) { context -> CypherProjectileRenderer(context) }
    }

//    @SubscribeEvent
//    fun registerRenderStateModifiers(event: RegisterRenderStateModifiersEvent) {
//    }

    @SubscribeEvent
    private fun registerLayerDefinitions(event: EntityRenderersEvent.RegisterLayerDefinitions) {

    }
}