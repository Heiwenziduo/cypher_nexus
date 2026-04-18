package com.github.heiwenziduo.untitled_world.api.registries

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.cyphers.AbstractCypher
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.DamageBoostCypher
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

/**
 *
 * */
object CypherRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<AbstractCypher>> =
        ResourceKey.createRegistryKey(UntitledWorld.modResource("cypher"))
    val REGISTRY: Registry<AbstractCypher> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<AbstractCypher> =
        DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        UntitledWorld.LOGGER.info("Registering Cypher...")
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    /* what's the meaning of this registry? map resources? */
    
    // projectile
    val SNOWBALL_CYPHER: SnowballCypher by DEFERRED_REGISTER.register("snowball") { -> SnowballCypher }

    // modifier
    val DAMAGE_BOOST_CYPHER: DamageBoostCypher by DEFERRED_REGISTER.register("damage_boost") { -> DamageBoostCypher }
}