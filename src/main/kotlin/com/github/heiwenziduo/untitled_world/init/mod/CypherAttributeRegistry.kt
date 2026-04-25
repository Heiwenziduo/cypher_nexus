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
    /** unit is "tick", cast to int at last */
    val CAST_DELAY = registerAttribute("cast_delay")
    /** unit is "tick", cast to int at last */
    val RECHARGE_TIME = registerAttribute("recharge_time")

    val DAMAGE = registerAttribute("damage")
    /** block per tick, but will show block/sec */
    val SPEED = registerAttribute("speed")
    /** degree */
    val SPREAD = registerAttribute("spread")
    val RECOIL = registerAttribute("recoil")
    val RADIUS = registerAttribute("redius")
    /** int, bounce times */
    val BOUNCE = registerAttribute("bounce")
    /** 1 -> 100% */
    val CRIT_CHANCE = registerAttribute("crit_chance")
}