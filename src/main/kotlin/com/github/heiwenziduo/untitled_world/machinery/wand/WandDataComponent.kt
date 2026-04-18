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
        Codec.FLOAT.fieldOf("manaCurrent").forGetter(WandData::manaCurrent),
        Codec.FLOAT.fieldOf("manaMax").forGetter(WandData::manaMax),
        Codec.FLOAT.fieldOf("manaRegn").forGetter(WandData::manaRegn),
        Codec.INT.fieldOf("capacity").forGetter(WandData::capacity),
        Codec.INT.fieldOf("draw").forGetter(WandData::draw),
    ).apply(it) { a,b,c,d,e -> WandData(a,b,c,d,e) } }

    val WAND_DATA_STREAM_CODEC: StreamCodec<ByteBuf, WandData> =
        StreamCodec.composite(
            ByteBufCodecs.FLOAT, WandData::manaCurrent,
            ByteBufCodecs.FLOAT, WandData::manaMax,
            ByteBufCodecs.FLOAT, WandData::manaRegn,
            ByteBufCodecs.INT, WandData::capacity,
            ByteBufCodecs.INT, WandData::draw,
            { a,b,c,d,e -> WandData(a,b,c,d,e) }
        )

    data class WandData(
        var manaCurrent: Float,
        val manaMax: Float,
        val manaRegn: Float,
        val capacity: Int,
        val draw: Int,
    ) {
        fun drainMana(value: Float) {

        }

        companion object {
            val DEFAULT = WandData(
                300f,
                300f,
                1.2f,
                4,
                1
            )
        }
    }
}