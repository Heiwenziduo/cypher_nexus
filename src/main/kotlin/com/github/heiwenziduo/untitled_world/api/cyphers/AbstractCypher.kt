package com.github.heiwenziduo.untitled_world.api.cyphers

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.api.cyphers.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import com.github.heiwenziduo.untitled_world.api.registries.CypherAttributeRegistry as Attrs

/**
 *
 * */
abstract class AbstractCypher(
//     val MANA_DRAIN: Float,
//     val CAST_DELAY: Int,
//     val RECHARGE_TIME: Int,
//     val DRAW: Int,
) {
//    abstract val MANA_DRAIN: Float
//    abstract val CAST_DELAY: Int
//    abstract val RECHARGE_TIME: Int
//    abstract val DRAW: Int


    /**
     * property-map marks every property this specific cypher (-type) may have.
     * base value not contained, which may be injected using json, or determine whether it calculates
     * */
    val ATTRIBUTE_MAP = HashMap<CypherAttribute<*>, CypherAttributeInstance<*>?>()
    var MAP_IS_LOCKED = false

    init {
        addProperty(Attrs.MANA_DRAIN)
        addProperty(Attrs.CAST_DELAY)
        addProperty(Attrs.RECHARGE_TIME)
        addProperty(Attrs.DRAW)
    }

    /**
     * Add a "key" to the map, its value needs to be filled via {#genAttributeInstance} manually.
     * This defines what attribute is available on the specific cypher
     * */
    fun addProperty(property: CypherAttribute<*>) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(property, null)
        else UntitledWorld.LOGGER.fatal("try add property $property while map is locked!")
    }

    fun genAttributeInstance() {
        if (!MAP_IS_LOCKED) {
            ATTRIBUTE_MAP.forEach{(attr, instanceMaybeNull) ->
                ATTRIBUTE_MAP[attr] = attr.instance()
            }

            MAP_IS_LOCKED = true
        } else {
            UntitledWorld.LOGGER.fatal("try genAttributeInstance while map is locked!")
        }
    }

    fun initializeData() {
        // TODO: read from CODEC
    }

    abstract fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper)
}