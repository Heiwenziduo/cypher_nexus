package com.github.heiwenziduo.cypher_nexus.network.clientbound

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.network.CNCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class OpenIndexScreen(
    val cyphersTotal: List<AbstractCypher>,
    val cyphersUnlocked: List<AbstractCypher>,
) : CustomPacketPayload {
    override fun type() = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<OpenIndexScreen> =
            CustomPacketPayload.Type(CypherNexus.modResource("open_index_screen"))

        val STREAM: StreamCodec<RegistryFriendlyByteBuf, OpenIndexScreen> = StreamCodec.composite(
            CNCodecs.CYPHER_LIST_STREAM, OpenIndexScreen::cyphersTotal,
            CNCodecs.CYPHER_LIST_STREAM, OpenIndexScreen::cyphersUnlocked,
            ::OpenIndexScreen
        )
    }
}