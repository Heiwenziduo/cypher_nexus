package com.github.heiwenziduo.untitled_world.machinery.wand

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/** temporary data for a wand */
object CastDataComponent {

    val CAST_DATA_CODEC: Codec<CastData> = RecordCodecBuilder.create { it.group(
        Codec.INT.fieldOf("index").forGetter(CastData::index),
        Codec.FLOAT.fieldOf("manaCurrent").forGetter(CastData::manaCurrent),
    ).apply(it) { i,c -> CastData(i,c) } }

    val CAST_DATA_STREAM_CODEC: StreamCodec<ByteBuf, CastData> =
        StreamCodec.composite(
            ByteBufCodecs.INT, CastData::index,
            ByteBufCodecs.FLOAT, CastData::manaCurrent,
            { i,c -> CastData(i,c) }
        )

    data class CastData(
        val index: Int,
        val manaCurrent: Float,
    ) {
        fun update(index: Int, manaCurrent: Float) = CastData(index, manaCurrent)

        companion object {
            val DEFAULT = CastData(0, 0f)
        }
    }
}