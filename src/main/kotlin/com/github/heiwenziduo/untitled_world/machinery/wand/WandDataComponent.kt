package com.github.heiwenziduo.untitled_world.machinery.wand

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec


/** persistent data for a wand */
object WandDataComponent {
    val WAND_DATA_CODEC: Codec<WandData> = RecordCodecBuilder.create{ it.group(
        Codec.FLOAT.fieldOf("manaMax").forGetter(WandData::manaMax),
        Codec.FLOAT.fieldOf("manaRegn").forGetter(WandData::manaRegn),
        Codec.INT.fieldOf("capacity").forGetter(WandData::capacity),
        Codec.INT.fieldOf("draw").forGetter(WandData::draw),
        Codec.FLOAT.fieldOf("wandLength").forGetter(WandData::wandLength),
        Codec.STRING.fieldOf("cypherList").forGetter(WandData::cypherList),
    ).apply(it) { a,b,c,d,e,f -> WandData(a,b,c,d,e,f) } }

    val WAND_DATA_STREAM_CODEC: StreamCodec<ByteBuf, WandData> =
        StreamCodec.composite(
            ByteBufCodecs.FLOAT, WandData::manaMax,
            ByteBufCodecs.FLOAT, WandData::manaRegn,
            ByteBufCodecs.INT, WandData::capacity,
            ByteBufCodecs.INT, WandData::draw,
            ByteBufCodecs.FLOAT, WandData::wandLength,
            ByteBufCodecs.STRING_UTF8, WandData::cypherList,
            { a,b,c,d,e,f -> WandData(a,b,c,d,e,f) }
        )

    data class WandData(
        val manaMax: Float,
        val manaRegn: Float,
        val capacity: Int,
        val draw: Int,
        val wandLength: Float,
        val cypherList: String
    ) {
        fun update(list: String) = WandData(manaMax, manaRegn, capacity, draw, wandLength, list)

        companion object {
            val DEFAULT = WandData(
                300f,
                1.2f,
                4,
                1,
                1f,
                ""
            )
        }
    }
}