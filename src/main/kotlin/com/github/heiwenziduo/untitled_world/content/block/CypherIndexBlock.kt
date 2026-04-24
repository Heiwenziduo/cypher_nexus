package com.github.heiwenziduo.untitled_world.content.block

import com.github.heiwenziduo.untitled_world.client.gui.CypherIndexScreen
import com.github.heiwenziduo.untitled_world.utility.data.CypherData
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

/**
 *
 * */
class CypherIndexBlock(): Block(
    Properties.of()
        .lightLevel { 15 }
        .strength(3.0f)
        .explosionResistance(1200.0f)
) {
    init {

    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        // triggered on both logical sides
        // UntitledWorld.LOGGER.info("SpellIndexBlock clicking, side: ${ if(level.isClientSide) "client" else "server" }")
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(CypherIndexScreen(CypherData.cypherMap))
        }
        return InteractionResult.SUCCESS
    }
}