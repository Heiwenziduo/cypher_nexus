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

    fun registerAttribute(path: String): Holder<CypherAttribute> =
        DEFERRED_REGISTER.register(path) { resource -> CypherAttribute(resource) }


//    val MANA_DRAIN = registerAttribute("mana_drain")
//    val DRAW = registerAttribute("draw")
    val CAST_DELAY = registerAttribute("cast_delay")
    val RECHARGE_TIME = registerAttribute("recharge_time")

    val DAMAGE = registerAttribute("damage")
    val SPEED = registerAttribute("speed")
    val SPREAD = registerAttribute("spread")
    val RECOIL = registerAttribute("recoil")
    val RADIUS = registerAttribute("redius")
    val BOUNCE = registerAttribute("bounce")
    val CRIT_CHANCE = registerAttribute("crit_chance")
}