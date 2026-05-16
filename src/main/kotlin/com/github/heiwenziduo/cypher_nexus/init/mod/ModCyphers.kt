package com.github.heiwenziduo.cypher_nexus.init.mod

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.cypher.SimpleModifier
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.HomingCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.DaedalusCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.FieryCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.modifier.PierceEntityCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.ArrowCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.EnderRecall
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher._TestModifier
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.EnderTeleportationCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.LlamaSpitCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.CypherNotFoundException
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.EmptyCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher._TestProjectile
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.CypherFlags
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

    // test things
    val T_T_T_Projectile = registerCypher(_TestProjectile)
    val T_T_T_Modifier = registerCypher(_TestModifier)

    // technical
    val EMPTY_CYPHER = registerCypher(EmptyCypher)

    // ==========================================================================================
    // # will present in register order #
    // ==========================================================================================

    // projectile
    val ARROW = registerCypher(ArrowCypher)
    val SNOWBALL = registerCypher(SnowballCypher)
    val ENDER_TELEPORTATION = registerCypher(EnderTeleportationCypher)
    val ENDER_RECALL = registerCypher(EnderRecall)
    val LLAMA_SPIT = registerCypher(LlamaSpitCypher)

    // static projectile

    // modifier
    val POWER = registerCypher(SimpleModifier(5f, "power")
        .attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 1.0)
        .attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 1.0))
    val BRISK = registerCypher(SimpleModifier(5f, "brisk").attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_TOTAL, 1.5))
    val FIERY = registerCypher(FieryCypher)
    val ANTIGRAVITY = registerCypher(SimpleModifier(1f, "antigravity").attribute(CypherAttributes.GRAVITY_FACTOR, CypherAttributeOperation.MULTIPLY_TOTAL, -1.0))




//    val FOCUS = registerCypher(SimpleModifier(1f, "focus").attribute(CypherAttributes.SPREAD, CypherAttributeOperation.ADD, -30.0))
////    val FIERCE = registerCypher(SimpleModifier(20f, "fierce").attribute(CypherAttributes.CRIT_CHANCE, CypherAttributeOperation.ADD, 0.15))
//    val POWER_IMBUE = registerCypher(SimpleModifier(66f, "power_imbue")
//        .attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.MULTIPLY_BASE, 0.25)
//        .attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.25)
//        .attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 1.0)
//        .attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 8.0))
//    val RECOIL_LESS = registerCypher(SimpleModifier(1f, "recoil_less").attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, -5.0))
//    val RECOIL_MORE = registerCypher(SimpleModifier(1f, "recoil_more").attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 5.0))
//    val RELOAD = registerCypher(SimpleModifier(12f, "reload")
//        .attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, -4.0)
//        .attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -6.0))




    val MANA_SURGE = registerCypher(SimpleModifier(-50f, "mana_surge")
        .attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 4.0)
        .attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 4.0))
    val HEAVILY_STRONG = registerCypher(SimpleModifier(20f, "heavily_strong")
        .attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.ADD, 4.0)
        .attribute(CypherAttributes.SPEED, CypherAttributeOperation.MULTIPLY_TOTAL, 0.75)
        .attribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 2.0)
        .attribute(CypherAttributes.GRAVITY_FACTOR, CypherAttributeOperation.ADD, 0.01))
    val PEACEFUL = registerCypher(SimpleModifier(5f, "peaceful").flag(CypherFlags.NO_DAMAGE))
    val BOUNCY = registerCypher(SimpleModifier(5f, "bouncy").attribute(CypherAttributes.BOUNCE, CypherAttributeOperation.ADD, 5.0))
    val NO_MORE_BOUNCE = registerCypher(SimpleModifier(0f, "no_more_bounce").attribute(CypherAttributes.BOUNCE, CypherAttributeOperation.SET, 0.0))
    val NO_MORE_DAMAGE = registerCypher(SimpleModifier(0f, "no_more_damage").attribute(CypherAttributes.DAMAGE, CypherAttributeOperation.MULTIPLY_TOTAL, 0.0))
    val EXTEND_TIME = registerCypher(SimpleModifier(35f, "extend_time")
        .attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 4.0)
        .attribute(CypherAttributes.EXISTING, CypherAttributeOperation.MULTIPLY_TOTAL, 1.25))
    val CURTAIL_TIME = registerCypher(SimpleModifier(35f, "curtail_time")
        .attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -4.0)
        .attribute(CypherAttributes.EXISTING, CypherAttributeOperation.MULTIPLY_TOTAL, 0.8))

    val HOMING = registerCypher(HomingCypher)
    val PIERCE_ENTITY = registerCypher(PierceEntityCypher)
    val DAEDALUS = registerCypher(DaedalusCypher)
    val NULLIFIER = registerCypher(SimpleModifier(44f, "nullifier")
        .attribute(CypherAttributes.EXISTING, CypherAttributeOperation.SET, 1.0)
        .attribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, -7.0)
        .attribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, -5.0))

    // passive

    // other
}