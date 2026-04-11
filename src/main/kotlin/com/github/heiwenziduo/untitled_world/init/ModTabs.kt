package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

object ModTabs {
    val DEFERRED_REGISTER: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val CREATIVE_MODE_TAB = DEFERRED_REGISTER.register(UntitledWorld.MOD_ID, { ->
        CreativeModeTab.builder()
            .title(UntitledWorld.modTranslation("item_group"))
            .icon { -> ModItems.BASIC_WAND.defaultInstance }
            .displayItems { parameters, output ->
                // add items while BuildCreativeModeTabContentsEvent is feasible too
                output.accept { ModItems.BASIC_WAND }
            }
            .build()
    })

}