package com.github.heiwenziduo.untitled_world.event

import com.github.heiwenziduo.untitled_world.Config
import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.utility.data.CypherData
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Blocks
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

/* @doc lifecycle:
 * mod constructor(FMLConstructModEvent) ->
 * registries(NewRegistryEvent, DataPackRegistryEvent.NewRegistry, RegisterEvent) ->
 * CommonSetup ->
 * sided setup(FMLClientSetupEvent, FMLDedicatedServerSetupEvent)
 * */
@EventBusSubscriber(modid = UntitledWorld.MOD_ID)
object LifeCycle {

    @SubscribeEvent
    private fun commonSetup(event: FMLCommonSetupEvent) {
        // this is a parallel dispatched event - we cannot interact with game state in this event.
        UntitledWorld.LOGGER.info("HELLO FROM COMMON SETUP")

        if (Config.logDirtBlock) UntitledWorld.LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT))
        UntitledWorld.LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber)

        // Config.items.forEach(Consumer { item: Item? -> LOGGER.info("ITEM >> {}", item.toString()) })

        CypherData.Companion.init()
    }
}