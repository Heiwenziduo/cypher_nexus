package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(UntitledWorld.MOD_ID)

    fun register() {
        REGISTRY.register(MOD_BUS)
    }

    val SPELL_INDEX_BLOCK_ITEM by REGISTRY.registerSimpleBlockItem("spell_index_block_item") { ->
        ModBlocks.SPELL_INDEX_BLOCK
    }
}