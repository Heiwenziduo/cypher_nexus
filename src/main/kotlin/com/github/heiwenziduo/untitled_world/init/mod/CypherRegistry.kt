package com.github.heiwenziduo.untitled_world.init.mod

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.DamageBoostCypher
import com.github.heiwenziduo.untitled_world.content.cypher.modifier.HomingCypher
import com.github.heiwenziduo.untitled_world.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
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
object CypherRegistry {
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


    // projectile
    val SNOWBALL_CYPHER = registerCypher(SnowballCypher)

    // static projectile

    // modifier
    val DAMAGE_BOOST_CYPHER = registerCypher(DamageBoostCypher)
    val HOMING_CYPHER = registerCypher(HomingCypher)

    // passive

    // other
}