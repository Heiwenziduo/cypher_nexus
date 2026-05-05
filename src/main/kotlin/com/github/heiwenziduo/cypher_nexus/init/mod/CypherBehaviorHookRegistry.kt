package com.github.heiwenziduo.cypher_nexus.init.mod

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.hook.CypherBehaviorHook
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object CypherBehaviorHookRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<CypherBehaviorHook>> =
        ResourceKey.createRegistryKey(CypherNexus.modResource("cypher/hook"))
    val REGISTRY: Registry<CypherBehaviorHook> = RegistryBuilder(RESOURCE_KEY).sync(true).create()

    val DEFERRED_REGISTER: DeferredRegister<CypherBehaviorHook> =
        DeferredRegister.create(REGISTRY, CypherNexus.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    fun registerBehavior(hook: CypherBehaviorHook): Holder<CypherBehaviorHook> {
        return DEFERRED_REGISTER.register(hook.resource.path) { -> hook }
    }
}