package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.wand.data.CastDataComponent
import com.github.heiwenziduo.untitled_world.machinery.wand.data.CastDataComponent.CAST_DATA_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.data.CastDataComponent.CAST_DATA_STREAM_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataComponent
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataComponent.WAND_DATA_CODEC
import com.github.heiwenziduo.untitled_world.machinery.wand.data.WandDataComponent.WAND_DATA_STREAM_CODEC
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.function.Supplier


object ModDataComponents {
    val DEFERRED_REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val WAND_DATA: Supplier<DataComponentType<WandDataComponent.WandData>> =
        DEFERRED_REGISTER.registerComponentType("wand_data")
        { it.persistent(WAND_DATA_CODEC).networkSynchronized(WAND_DATA_STREAM_CODEC) }

    val CAST_DATA: Supplier<DataComponentType<CastDataComponent.CastData>> =
        DEFERRED_REGISTER.registerComponentType("cast_data")
        { it.persistent(CAST_DATA_CODEC).networkSynchronized(CAST_DATA_STREAM_CODEC) }
}