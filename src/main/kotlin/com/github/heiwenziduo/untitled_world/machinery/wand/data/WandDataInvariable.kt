package com.github.heiwenziduo.untitled_world.machinery.wand.data

import com.github.heiwenziduo.untitled_world.init.mod.ModCyphers
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec


/**
 * holds wand invariable data, separate into chunks
 * */
data class WandDataInvariable(val chunk0: WandDataChunk0, val chunk1: WandDataChunk1, val chunk2: WandDataChunk2) {
    data class WandDataChunk0(val maxMana: Float, val manaRegen: Float, val wandLength: Float)
    data class WandDataChunk1(val capacity: Int, val draw: Int, val castDelay: Int, val recharge: Int,)
    data class WandDataChunk2(val alwaysCast: List<AbstractCypher>)

    companion object {
        val CHUNK0_CODEX: Codec<WandDataChunk0> = RecordCodecBuilder.create { it.group(
            Codec.FLOAT.fieldOf("manaMax").forGetter(WandDataChunk0::maxMana),
            Codec.FLOAT.fieldOf("manaRegen").forGetter(WandDataChunk0::manaRegen),
            Codec.FLOAT.fieldOf("wandLength").forGetter(WandDataChunk0::wandLength),
        ).apply(it, ::WandDataChunk0) }
        val CHUNK1_CODEX: Codec<WandDataChunk1> = RecordCodecBuilder.create { it.group(
            Codec.INT.fieldOf("capacity").forGetter(WandDataChunk1::capacity),
            Codec.INT.fieldOf("draw").forGetter(WandDataChunk1::draw),
            Codec.INT.fieldOf("castDelay").forGetter(WandDataChunk1::castDelay),
            Codec.INT.fieldOf("recharge").forGetter(WandDataChunk1::recharge),
        ).apply(it, ::WandDataChunk1) }
        val CHUNK2_CODEX: Codec<WandDataChunk2> = RecordCodecBuilder.create { it.group(
            ModCyphers.REGISTRY
                .byNameCodec().listOf()
                .fieldOf("alwaysCast")
                .forGetter(WandDataChunk2::alwaysCast)
        ).apply(it, ::WandDataChunk2) }

        val INVARIABLE_DATA_CODEC: Codec<WandDataInvariable> = RecordCodecBuilder.create { it.group(
            CHUNK0_CODEX.fieldOf("chunk0").forGetter(WandDataInvariable::chunk0),
            CHUNK1_CODEX.fieldOf("chunk1").forGetter(WandDataInvariable::chunk1),
            CHUNK2_CODEX.fieldOf("chunk2").forGetter(WandDataInvariable::chunk2),
        ).apply(it, ::WandDataInvariable) }


        val CHUNK0_STREAM: StreamCodec<ByteBuf, WandDataChunk0> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, WandDataChunk0::maxMana,
            ByteBufCodecs.FLOAT, WandDataChunk0::manaRegen,
            ByteBufCodecs.FLOAT, WandDataChunk0::wandLength,
            ::WandDataChunk0)
        val CHUNK1_STREAM: StreamCodec<ByteBuf, WandDataChunk1> = StreamCodec.composite(
            ByteBufCodecs.INT, WandDataChunk1::capacity,
            ByteBufCodecs.INT, WandDataChunk1::draw,
            ByteBufCodecs.INT, WandDataChunk1::castDelay,
            ByteBufCodecs.INT, WandDataChunk1::recharge,
            ::WandDataChunk1)
        val CHUNK2_STREAM: StreamCodec<RegistryFriendlyByteBuf, WandDataChunk2> =
            ByteBufCodecs.registry(ModCyphers.RESOURCE_KEY).apply(ByteBufCodecs.list())
                .map(::WandDataChunk2, WandDataChunk2::alwaysCast)

        val INVARIABLE_DATA_STREAM: StreamCodec<RegistryFriendlyByteBuf, WandDataInvariable> = StreamCodec.composite(
            CHUNK0_STREAM, WandDataInvariable::chunk0,
            CHUNK1_STREAM, WandDataInvariable::chunk1,
            CHUNK2_STREAM, WandDataInvariable::chunk2,
            ::WandDataInvariable
        )


        val DEFAULT = WandDataInvariable(
            WandDataChunk0(300f, 1.5f, 1.2f),
            WandDataChunk1(6, 1, 12, 15),
            WandDataChunk2(listOf())
        )
    }
}