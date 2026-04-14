package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.item.BasicWandItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val DEFERRED_REGISTER: DeferredRegister.Items = DeferredRegister.createItems(UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val BASIC_WAND: BasicWandItem by DEFERRED_REGISTER.register("basic_wand", { -> BasicWandItem.testWand()})

    // When it comes to mass, guess I can make a factory function to auto register block-item.
    val CYPHER_INDEX_BLOCK_ITEM by DEFERRED_REGISTER.registerSimpleBlockItem("cypher_index") { ->
        ModBlocks.CYPHER_INDEX_BLOCK
    }
}