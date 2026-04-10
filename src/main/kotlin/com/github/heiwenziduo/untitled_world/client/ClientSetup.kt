package com.github.heiwenziduo.untitled_world.client

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.UntitledWorld.LOGGER
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@EventBusSubscriber(modid = UntitledWorld.MOD_ID, value = [Dist.CLIENT])
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
}