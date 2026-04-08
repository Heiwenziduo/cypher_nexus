package com.github.heiwenziduo.untitled_world

import com.github.heiwenziduo.untitled_world.init.ModBlocks
import com.github.heiwenziduo.untitled_world.init.ModItems
import com.github.heiwenziduo.untitled_world.init.mod.CipherRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Blocks
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.time.Duration.Companion.seconds


/*
* An entry in neoforge.mods.toml does not need a corresponding @Mod annotation.
 * Likewise, an entry in the neoforge.mods.toml can have multiple @Mod annotations,
 * for example if you want to separate common logic and client only logic.
* */
@Mod(UntitledWorld.MOD_ID)
@EventBusSubscriber
object UntitledWorld {
    const val MOD_ID: String = "untitled_world"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    fun modResource(path: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

    init {
        LOGGER.log(Level.INFO, "Hello world!")
        ModBlocks.register()
        ModItems.register()

        CipherRegistry.register()
//        // Kotlin style events register
//        val obj = runForDist(
//            clientTarget = {
//                MOD_BUS.addListener(::onClientSetup)
//                Minecraft.getInstance()
//            },
//            serverTarget = {
//                MOD_BUS.addListener(::onServerSetup)
//                "test"
//            })
//
//        println(obj) // Minecraft

        CoroutineScope(Dispatchers.Default).launch {
            LOGGER.log(Level.INFO, "Before delay")
            delay(5.seconds)
            LOGGER.log(Level.INFO, "After 5 seconds")
        }

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Untitled_world) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        // NeoForge.EVENT_BUS.register(this)
    }
//    /**
//     * This is used for initializing client specific
//     * things such as renderers and keymaps
//     * Fired on the mod specific event bus.
//     */
//    private fun onClientSetup(event: FMLClientSetupEvent) {
//        LOGGER.log(Level.INFO, "HELLO, Initializing client...")
//    }

//    /**
//     * Fired on the global Forge bus.
//     */
//    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
//        LOGGER.log(Level.INFO, "HELLO, Server starting...")
//    }

    @SubscribeEvent
    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("HELLO FROM COMMON SETUP")

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT))
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber)

        // Config.items.forEach(Consumer { item: Item? -> LOGGER.info("ITEM >> {}", item.toString()) })
    }

    @SubscribeEvent
    private fun onClientStarting(event: FMLClientSetupEvent) {
        LOGGER.info("HELLO FROM CLIENT SETUP")
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
    }
    @SubscribeEvent
    private fun onServerStarting(event: ServerStartingEvent) {
        LOGGER.info("HELLO from server starting")
    }

    @SubscribeEvent // on the mod event bus
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(CipherRegistry.REGISTRY)
    }
}