package com.github.heiwenziduo.cypher_nexus.init.mod

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.cypher.SimpleModifier
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.HomingCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.PierceEntityCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher._TestModifier
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.EnderTeleportationCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.CypherNotFoundException
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.EmptyCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 *
 * */
object ModCyphers {
    val RESOURCE_KEY: ResourceKey<Registry<AbstractCypher>> =
        ResourceKey.createRegistryKey(CypherNexus.modResource("cypher"))
    val REGISTRY: Registry<AbstractCypher> = RegistryBuilder(RESOURCE_KEY).sync(true).defaultKey(EmptyCypher.resource).create()

    val DEFERRED_REGISTER: DeferredRegister<AbstractCypher> =
        DeferredRegister.create(REGISTRY, CypherNexus.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    fun registerCypher(cypher: AbstractCypher): Holder<AbstractCypher> {
        return DEFERRED_REGISTER.register(cypher.resource.path) { -> cypher }
    }

    fun getCypher(resource: ResourceLocation): AbstractCypher? = REGISTRY.get(resource)
    fun getCypherOrThrow(resource: ResourceLocation): AbstractCypher {
        val c = REGISTRY.get(resource)
        if (c == null) throw CypherNotFoundException("missing cypher: ${resource.namespace}-${resource.path}")
        return c
    }


    val EMPTY_CYPHER = registerCypher(EmptyCypher) // technical

    // TODO should these be sorted by functionality? or just by alphabet?

    // projectile
    val SNOWBALL = registerCypher(SnowballCypher)
    val ENDER_TELEPORTATION = registerCypher(EnderTeleportationCypher)

    // static projectile

    // modifier
    val T_T_T_Modifier = registerCypher(_TestModifier)

    val ACCELERATION = registerCypher(SimpleModifier(10f, "acceleration").attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.5))
    val BOUNCY = registerCypher(SimpleModifier(5f, "bouncy").attribute(CypherAttributes.BOUNCE, CypherAttributeOperation.ADD, 5.0))
    val DAMAGE_BOOST = registerCypher(SimpleModifier(20f, "damage_boost").attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 1.0).attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 1.0))
    val FOCUS = registerCypher(SimpleModifier(1f, "focus").attribute(CypherAttributes.SPREAD, CypherAttributeOperation.ADD, -30.0))
    val FIERCE = registerCypher(SimpleModifier(20f, "fierce").attribute(CypherAttributes.CRIT_CHANCE, CypherAttributeOperation.ADD, 0.15))
    val HEAVY_SHOOT = registerCypher(SimpleModifier(20f, "heavy_shoot").attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 5.0).attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_TOTAL, 0.5))
    val MANA_SURGE = registerCypher(SimpleModifier(-66f, "mana_surge").attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 4.0))
    val NO_BOUNCE = registerCypher(SimpleModifier(0f, "no_bounce").attribute(CypherAttributes.BOUNCE, CypherAttributeOperation.SET, 0.0))
    val POWER_IMBUE = registerCypher(SimpleModifier(66f, "power_imbue").attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.MULTIPLY_BASE, 0.25).attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.25).attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 1.0).attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 8.0))
    val RECOIL_LESS = registerCypher(SimpleModifier(1f, "recoil_less").attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, -5.0))
    val RECOIL_MORE = registerCypher(SimpleModifier(1f, "recoil_more").attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 5.0))
    val RELOAD = registerCypher(SimpleModifier(12f, "reload").attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, -4.0).attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -6.0))
    val STOPWATCH = registerCypher(SimpleModifier(35f, "stopwatch").attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -4.0).attribute(CypherAttributes.EXISTING, CypherAttributeOperation.MULTIPLY_TOTAL, 0.8))
    val TIMER = registerCypher(SimpleModifier(35f, "timer").attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 4.0).attribute(CypherAttributes.EXISTING, CypherAttributeOperation.MULTIPLY_TOTAL, 1.2))
    val ZO = registerCypher(SimpleModifier(44f, "zo").attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, -2.0).attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -3.0).attribute(CypherAttributes.EXISTING, CypherAttributeOperation.SET, 1.0))

    val HOMING = registerCypher(HomingCypher)
    val PIERCE_ENTITY = registerCypher(PierceEntityCypher)

    // passive

    // other
}