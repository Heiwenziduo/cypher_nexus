package com.github.heiwenziduo.untitled_world.init.mod

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.RegistryBuilder
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * use to create pools, to display, or to roll for some sake
 * */
object CypherCategoryRegistry {
    val RESOURCE_KEY: ResourceKey<Registry<CypherCategory>> =
        ResourceKey.createRegistryKey(UntitledWorld.modResource("cypher/category"))
    val REGISTRY: Registry<CypherCategory> = RegistryBuilder(RESOURCE_KEY).create()

    val DEFERRED_REGISTER: DeferredRegister<CypherCategory> =
        DeferredRegister.create(REGISTRY, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    fun registerCategory(category: CypherCategory): Holder<CypherCategory> =
        DEFERRED_REGISTER.register(category.resource.path) { -> category }

    fun getCategory(resource: ResourceLocation): CypherCategory? = REGISTRY.get(resource)

    val PROJECTILE_RESOURCE = UntitledWorld.modResource("projectile")
    val STATIC_PROJECTILE_RESOURCE = UntitledWorld.modResource("static_projectile")
    val MODIFIER_RESOURCE = UntitledWorld.modResource("modifier")
    val OTHER_RESOURCE = UntitledWorld.modResource("other")


    val PROJECTILE = registerCategory(CypherCategory(
        PROJECTILE_RESOURCE
    ))

    val STATIC_PROJECTILE = registerCategory(CypherCategory(
        STATIC_PROJECTILE_RESOURCE
    ))

    val MODIFIER = registerCategory(CypherCategory(
        MODIFIER_RESOURCE
    ))

    val OTHER = registerCategory(CypherCategory(
        OTHER_RESOURCE
    ))
}