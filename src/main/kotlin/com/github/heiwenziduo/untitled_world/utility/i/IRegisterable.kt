package com.github.heiwenziduo.untitled_world.utility.i

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

/**  */
interface IRegisterable {
    val resource: ResourceLocation
    fun registryName(): String = resource.path
    fun translation(): MutableComponent = Component.empty()
}