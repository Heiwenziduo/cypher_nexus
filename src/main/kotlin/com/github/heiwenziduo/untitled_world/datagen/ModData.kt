package com.github.heiwenziduo.untitled_world.datagen

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.datagen.provider.ModBlockModelProvider
import com.github.heiwenziduo.untitled_world.datagen.provider.ModBlockStateProvider
import com.github.heiwenziduo.untitled_world.datagen.provider.ModItemModelProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent


/**
 * an object singleton that handle data-relevant events
 * */
@EventBusSubscriber(modid = UntitledWorld.MOD_ID)
object ModData {

    /* @doc
     * on higher versions (1.21.11) GatherDataEvent.Client & GatherDataEvent.Server are independent,
     * generating them by running the runClientData and runServerData tasks, respectively.
     * */
    // on the mod event bus
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        // DataGenerator, that we register the providers to.
        val generator = event.generator
        // PackOutput, used by some providers to determine their file output location.
        val output = generator.packOutput
        // ExistingFileHelper, used by providers for things that can reference other files.
        val existingFileHelper = event.existingFileHelper
        // CompletableFuture<HolderLookup.Provider>,
        // mainly used by tags and datagen registries to reference other, potentially not yet existing elements.
        val lookupProvider = event.lookupProvider

        val server = event.includeServer()
        val client = event.includeClient()

        // assets on the client
        generator.addProvider(client, ModItemModelProvider(output, existingFileHelper))
        // generator.addProvider(client, ModBlockModelProvider(output, existingFileHelper))
        generator.addProvider(client, ModBlockStateProvider(output, existingFileHelper))
        // and data on the server
    }
}