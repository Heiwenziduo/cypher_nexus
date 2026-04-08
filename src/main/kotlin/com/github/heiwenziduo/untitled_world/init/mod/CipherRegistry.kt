package com.github.heiwenziduo.untitled_world.init.mod

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.UntitledWorld.modResource
import com.github.heiwenziduo.untitled_world.api.ciphers.AbstractCipher
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 *
 * */
object CipherRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<AbstractCipher>> =
        ResourceKey.createRegistryKey(modResource("cipher"))
    val REGISTRY: Registry<AbstractCipher> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER = DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        UntitledWorld.LOGGER.info("Registering Ciphers...")
        DEFERRED_REGISTER.register(MOD_BUS)
    }
}