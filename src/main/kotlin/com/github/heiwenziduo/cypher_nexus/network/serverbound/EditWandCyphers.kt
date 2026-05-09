package com.github.heiwenziduo.cypher_nexus.network.serverbound

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.network.CNCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class EditWandCyphers(
    val uuid: String,
    val cyphers : List<AbstractCypher>
) : CustomPacketPayload {
    override fun type() = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<EditWandCyphers> =
            CustomPacketPayload.Type(CypherNexus.modResource("edit_wand_cyphers"))

        val STREAM: StreamCodec<RegistryFriendlyByteBuf, EditWandCyphers> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, EditWandCyphers::uuid,
            CNCodecs.CYPHER_LIST_STREAM, EditWandCyphers::cyphers,
            ::EditWandCyphers
        )
    }
}