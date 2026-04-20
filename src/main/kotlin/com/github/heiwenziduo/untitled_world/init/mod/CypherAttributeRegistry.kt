package com.github.heiwenziduo.untitled_world.init.mod

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

/**
 * registry attribute keys
 * */
object CypherAttributeRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<CypherAttribute>> =
        ResourceKey.createRegistryKey(UntitledWorld.modResource("cypher/attribute"))
    val REGISTRY: Registry<CypherAttribute> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<CypherAttribute> =
        DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }


    val MANA_DRAIN: Holder<CypherAttribute> = DEFERRED_REGISTER.register("mana_drain") { resource -> CypherAttribute(resource) }
    val CAST_DELAY: Holder<CypherAttribute> = DEFERRED_REGISTER.register("cast_delay") { resource -> CypherAttribute(resource) }
    val RECHARGE_TIME: Holder<CypherAttribute> = DEFERRED_REGISTER.register("recharge_time") { resource -> CypherAttribute(resource) }
    val DRAW: Holder<CypherAttribute> = DEFERRED_REGISTER.register("draw") { resource -> CypherAttribute(resource) }

    val DAMAGE by DEFERRED_REGISTER.register("damage") { resource -> CypherAttribute(resource) }
    val SPEED by DEFERRED_REGISTER.register("speed") { resource -> CypherAttribute(resource) }
    val SPREAD by DEFERRED_REGISTER.register("spread") { resource -> CypherAttribute(resource) }
    val RECOIL by DEFERRED_REGISTER.register("recoil") { resource -> CypherAttribute(resource) }
    val RADIUS by DEFERRED_REGISTER.register("redius") { resource -> CypherAttribute(resource) }
    val BOUNCE by DEFERRED_REGISTER.register("bounce") { resource -> CypherAttribute(resource) }
    val CRIT_CHANCE by DEFERRED_REGISTER.register("crit_chance") { resource -> CypherAttribute(resource) }
}