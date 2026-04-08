package com.github.heiwenziduo.untitled_world.init

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModBlocks {
    val DEFERRED_REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(UntitledWorld.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    /*
    * DeferredHolder<R, T extends R> is a subclass of Supplier<T>.
    * "by" make it automatically go inside the container and get the Block itself instead of a DeferredHolder<Block, T extends Block>.
    * */
    val SPELL_INDEX_BLOCK by DEFERRED_REGISTER.register("spell_index_block") { ->
        Block(BlockBehaviour.Properties.of().lightLevel { 15 }.strength(3.0f))
    }
}