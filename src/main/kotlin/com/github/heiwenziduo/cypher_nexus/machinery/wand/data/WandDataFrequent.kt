package com.github.heiwenziduo.cypher_nexus.machinery.wand.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class WandDataFrequent (val manaCurrent: Float, val index: Int, ) {

    companion object {
        val FREQUENT_DATA_CODEC: Codec<WandDataFrequent> = RecordCodecBuilder.create { it.group(
            Codec.FLOAT.fieldOf("manaCurrent").forGetter(WandDataFrequent::manaCurrent),
            Codec.INT.fieldOf("index").forGetter(WandDataFrequent::index),
        ).apply(it, ::WandDataFrequent) }

        val FREQUENT_DATA_STREAM: StreamCodec<ByteBuf, WandDataFrequent> = StreamCodec.composite(
                ByteBufCodecs.FLOAT, WandDataFrequent::manaCurrent,
            ByteBufCodecs.INT, WandDataFrequent::index,
                ::WandDataFrequent)

        val DEFAULT = WandDataFrequent(0f, 0)
    }
}