package com.github.heiwenziduo.cypher_nexus.machinery.wand.data

import com.github.heiwenziduo.cypher_nexus.init.mod.ModCyphers
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec


/**
 * holds wand invariable data, separate into chunks
 * */
data class WandDataInvariable(val chunkF: WandDataChunkF, val chunkI: WandDataChunkI, val chunkL: WandDataChunkL) {
    data class WandDataChunkF(val maxMana: Float, val manaRegen: Float, val wandLength: Float,)
    data class WandDataChunkI(val capacity: Int, val draw: Int, val castDelay: Int, val rechargeTime: Int,)
    data class WandDataChunkL(val alwaysCast: List<AbstractCypher>)

    companion object {
        val CHUNK0_CODEX: Codec<WandDataChunkF> = RecordCodecBuilder.create { it.group(
            Codec.FLOAT.fieldOf("manaMax").forGetter(WandDataChunkF::maxMana),
            Codec.FLOAT.fieldOf("manaRegen").forGetter(WandDataChunkF::manaRegen),
            Codec.FLOAT.fieldOf("wandLength").forGetter(WandDataChunkF::wandLength),
        ).apply(it, ::WandDataChunkF) }
        val CHUNK1_CODEX: Codec<WandDataChunkI> = RecordCodecBuilder.create { it.group(
            Codec.INT.fieldOf("capacity").forGetter(WandDataChunkI::capacity),
            Codec.INT.fieldOf("draw").forGetter(WandDataChunkI::draw),
            Codec.INT.fieldOf("castDelay").forGetter(WandDataChunkI::castDelay),
            Codec.INT.fieldOf("rechargeTime").forGetter(WandDataChunkI::rechargeTime),
        ).apply(it, ::WandDataChunkI) }
        val CHUNK2_CODEX: Codec<WandDataChunkL> = RecordCodecBuilder.create { it.group(
            ModCyphers.REGISTRY
                .byNameCodec().listOf()
                .fieldOf("alwaysCast")
                .forGetter(WandDataChunkL::alwaysCast)
        ).apply(it, ::WandDataChunkL) }

        val INVARIABLE_DATA_CODEC: Codec<WandDataInvariable> = RecordCodecBuilder.create { it.group(
            CHUNK0_CODEX.fieldOf("chunk0").forGetter(WandDataInvariable::chunkF),
            CHUNK1_CODEX.fieldOf("chunk1").forGetter(WandDataInvariable::chunkI),
            CHUNK2_CODEX.fieldOf("chunk2").forGetter(WandDataInvariable::chunkL),
        ).apply(it, ::WandDataInvariable) }


        val CHUNK0_STREAM: StreamCodec<ByteBuf, WandDataChunkF> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, WandDataChunkF::maxMana,
            ByteBufCodecs.FLOAT, WandDataChunkF::manaRegen,
            ByteBufCodecs.FLOAT, WandDataChunkF::wandLength,
            ::WandDataChunkF)
        val CHUNK1_STREAM: StreamCodec<ByteBuf, WandDataChunkI> = StreamCodec.composite(
            ByteBufCodecs.INT, WandDataChunkI::capacity,
            ByteBufCodecs.INT, WandDataChunkI::draw,
            ByteBufCodecs.INT, WandDataChunkI::castDelay,
            ByteBufCodecs.INT, WandDataChunkI::rechargeTime,
            ::WandDataChunkI)
        val CHUNK2_STREAM: StreamCodec<RegistryFriendlyByteBuf, WandDataChunkL> =
            ByteBufCodecs.registry(ModCyphers.RESOURCE_KEY).apply(ByteBufCodecs.list())
                .map(::WandDataChunkL, WandDataChunkL::alwaysCast)

        val INVARIABLE_DATA_STREAM: StreamCodec<RegistryFriendlyByteBuf, WandDataInvariable> = StreamCodec.composite(
            CHUNK0_STREAM, WandDataInvariable::chunkF,
            CHUNK1_STREAM, WandDataInvariable::chunkI,
            CHUNK2_STREAM, WandDataInvariable::chunkL,
            ::WandDataInvariable
        )


        val DEFAULT = WandDataInvariable(
            WandDataChunkF(300f, 3f, 1.2f),
            WandDataChunkI(6, 1, 12, 15),
            WandDataChunkL(listOf())
        )
    }
}