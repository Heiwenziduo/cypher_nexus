package com.github.heiwenziduo.untitled_world.utility.i

import net.minecraft.resources.ResourceLocation

/**  */
interface IRegisterable {
    val resource: ResourceLocation
    fun registryName(): String = resource.path
}