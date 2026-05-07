package com.github.heiwenziduo.cypher_nexus.network

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class OpenIndexScreen(
    val listTotal: List<AbstractCypher>,
    val listUnlock: List<AbstractCypher>,
) : CustomPacketPayload {
    override fun type() = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<OpenIndexScreen> =
            CustomPacketPayload.Type(CypherNexus.modResource("open_index_screen"))

        val STREAM: StreamCodec<RegistryFriendlyByteBuf, OpenIndexScreen> = StreamCodec.composite(
            CNCodecs.CYPHER_LIST_STREAM, OpenIndexScreen::listTotal,
            CNCodecs.CYPHER_LIST_STREAM, OpenIndexScreen::listUnlock,
            ::OpenIndexScreen
        )
    }
}