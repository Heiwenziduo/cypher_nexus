package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val DEFERRED_REGISTER: DeferredRegister.Items = DeferredRegister.createItems(UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    // When it comes to mass, guess I can make a factory function to auto register block-item.
    val SPELL_INDEX_BLOCK_ITEM by DEFERRED_REGISTER.registerSimpleBlockItem("cypher_index_block") { ->
        ModBlocks.CYPHER_INDEX_BLOCK
    }
}