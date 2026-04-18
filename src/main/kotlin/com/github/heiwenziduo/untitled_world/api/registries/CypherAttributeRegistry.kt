package com.github.heiwenziduo.untitled_world.api.registries

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttribute
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

/**
 * registry attribute keys
 * */
object CypherAttributeRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<CypherAttribute<*>>> =
        ResourceKey.createRegistryKey(UntitledWorld.modResource("cypher/attribute"))
    val REGISTRY: Registry<CypherAttribute<*>> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<CypherAttribute<*>> =
        DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        UntitledWorld.LOGGER.info("Registering Cypher Attribute...")
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val MANA_DRAIN by DEFERRED_REGISTER.register("mana_drain") { resource -> CypherAttribute<Float>(resource) }
    val CAST_DELAY by DEFERRED_REGISTER.register("cast_delay") { resource -> CypherAttribute<Int>(resource) }
    val RECHARGE_TIME by DEFERRED_REGISTER.register("recharge_time") { resource -> CypherAttribute<Int>(resource) }
    val DRAW by DEFERRED_REGISTER.register("draw") { resource -> CypherAttribute<Short>(resource) }

    val DAMAGE by DEFERRED_REGISTER.register("damage") { resource -> CypherAttribute<Double>(resource) }
    val SPEED by DEFERRED_REGISTER.register("speed") { resource -> CypherAttribute<Float>(resource) }
    val SPREAD by DEFERRED_REGISTER.register("spread") { resource -> CypherAttribute<Float>(resource) }
    val RECOIL by DEFERRED_REGISTER.register("recoil") { resource -> CypherAttribute<Float>(resource) }
    val RADIUS by DEFERRED_REGISTER.register("redius") { resource -> CypherAttribute<Float>(resource) }
    val BOUNCE by DEFERRED_REGISTER.register("bounce") { resource -> CypherAttribute<Int>(resource) }
    val CRIT_CHANCE by DEFERRED_REGISTER.register("crit_chance") { resource -> CypherAttribute<Float>(resource) }
}