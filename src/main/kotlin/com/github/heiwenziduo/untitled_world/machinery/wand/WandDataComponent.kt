package com.github.heiwenziduo.untitled_world.machinery.wand

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.function.BiFunction


/** data component */
object WandDataComponent {
    val WAND_DATA_CODEC: Codec<WandData> = RecordCodecBuilder.create{ it.group(
        Codec.INT.fieldOf("index").forGetter(WandData::index),
        Codec.FLOAT.fieldOf("manaCurrent").forGetter(WandData::manaCurrent),
        Codec.FLOAT.fieldOf("manaMax").forGetter(WandData::manaMax),
        Codec.FLOAT.fieldOf("manaRegn").forGetter(WandData::manaRegn),
        Codec.INT.fieldOf("capacity").forGetter(WandData::capacity),
        Codec.INT.fieldOf("draw").forGetter(WandData::draw),
    ).apply(it) { a,b,c,d,e,f -> WandData(a,b,c,d,e,f) } }

    val WAND_DATA_STREAM_CODEC: StreamCodec<ByteBuf, WandData> =
        StreamCodec.composite(
            ByteBufCodecs.INT, WandData::index,
            ByteBufCodecs.FLOAT, WandData::manaCurrent,
            ByteBufCodecs.FLOAT, WandData::manaMax,
            ByteBufCodecs.FLOAT, WandData::manaRegn,
            ByteBufCodecs.INT, WandData::capacity,
            ByteBufCodecs.INT, WandData::draw,
            { a,b,c,d,e,f -> WandData(a,b,c,d,e,f) }
        )

    data class WandData(
        val index: Int,
        val manaCurrent: Float,
        val manaMax: Float,
        val manaRegn: Float,
        val capacity: Int,
        val draw: Int,
    ) {
        // TODO maybe separate variables and invariables?
        fun update(index: Int, manaCurrent: Float): WandData {
            return WandData(index, manaCurrent, manaMax, manaRegn, capacity, draw)
        }

        companion object {
            val DEFAULT = WandData(
                0,
                300f,
                300f,
                1.2f,
                4,
                1
            )
        }
    }
}