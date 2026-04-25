package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.wand.CastDataComponent.CAST_DATA_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.CastDataComponent.CAST_DATA_STREAM_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.WandDataComponent.WAND_DATA_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.WandDataComponent.WAND_DATA_STREAM_CODEC
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS


object ModDataComponents {
    val DEFERRED_REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val WAND_DATA = DEFERRED_REGISTER.registerComponentType(
        "wand_data"
    ) { it.persistent(WAND_DATA_CODEC).networkSynchronized(WAND_DATA_STREAM_CODEC) }

    val CAST_DATA = DEFERRED_REGISTER.registerComponentType(
        "cast_data"
    ) { it.persistent(CAST_DATA_CODEC).networkSynchronized(CAST_DATA_STREAM_CODEC) }
}