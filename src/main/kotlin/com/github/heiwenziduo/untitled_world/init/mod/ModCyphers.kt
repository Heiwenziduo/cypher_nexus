package com.github.heiwenziduo.untitled_world.init.mod

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.cypher.SimpleModifier
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.HomingCypher
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
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
        ResourceKey.createRegistryKey(UntitledWorld.modResource("cypher"))
    val REGISTRY: Registry<AbstractCypher> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<AbstractCypher> =
        DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    fun registerCypher(cypher: AbstractCypher): Holder<AbstractCypher> {
        return DEFERRED_REGISTER.register(cypher.resource.path) { -> cypher }
    }

    fun getCypher(resource: ResourceLocation): AbstractCypher? = REGISTRY.get(resource)


    // TODO should these be sorted by functionality? or just by alphabet?

    // projectile
    val SNOWBALL_CYPHER = registerCypher(SnowballCypher)

    // static projectile

    // modifier
    val ACCELERATION_CYPHER = registerCypher(SimpleModifier(10f, "acceleration").attribute(CypherAttributeRegistry.SPEED, CypherAttributeOperation.MULTIPLY_BASE, 0.5))
    val DAMAGE_BOOST_CYPHER = registerCypher(SimpleModifier(20f, "damage_boost").attribute(CypherAttributeRegistry.DAMAGE, CypherAttributeOperation.ADD, 1.0))
    val FOCUS_CYPHER = registerCypher(SimpleModifier(1f, "focus").attribute(CypherAttributeRegistry.SPREAD, CypherAttributeOperation.ADD, -30.0))
    val RECOIL_LESS_CYPHER = registerCypher(SimpleModifier(1f, "recoil_less").attribute(CypherAttributeRegistry.RECOIL, CypherAttributeOperation.ADD, -5.0))
    val RECOIL_MORE_CYPHER = registerCypher(SimpleModifier(1f, "recoil_more").attribute(CypherAttributeRegistry.RECOIL, CypherAttributeOperation.ADD, 5.0))

    val HOMING_CYPHER = registerCypher(HomingCypher)

    // passive

    // other
}