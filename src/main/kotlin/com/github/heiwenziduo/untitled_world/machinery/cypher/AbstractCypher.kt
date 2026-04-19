package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.content.cypher.CypherModifierHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

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
    protected val ATTRIBUTE_MAP = HashMap<CypherAttribute<*>, CypherAttributeInstance<*>?>()
    private var MAP_IS_LOCKED = false
    // TODO
    val CATEGORY = 0

    init {
        // crash, should I use DeferredHolder?
//        addAttribute(Attrs.MANA_DRAIN)
//        addAttribute(Attrs.CAST_DELAY)
//        addAttribute(Attrs.RECHARGE_TIME)
//        addAttribute(Attrs.DRAW)
    }

    /**
     * Add a "key" to the map, its value needs to be filled via {#genAttributeInstance} manually.
     * This defines what attribute is available on the specific cypher
     * */
    protected fun addAttribute(property: CypherAttribute<*>) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(property, null)
        else UntitledWorld.LOGGER.fatal("try add property $property while map is locked!")
    }

    /**
     * add attribute with default value
     * TODO
     * */
    protected fun <T : Number> addAttribute(property: CypherAttribute<T>, value: T) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(property, null)
        else UntitledWorld.LOGGER.fatal("try add property $property while map is locked!")
    }

    /**
     * should be called after any subclass initialization
     * TODO: Can it be managed here centrally?
     * */
    fun genAttributeInstance(): AbstractCypher {
        if (!MAP_IS_LOCKED) {
            ATTRIBUTE_MAP.forEach{(attr, instanceMaybeNull) ->
                ATTRIBUTE_MAP[attr] = attr.instance()
            }

            MAP_IS_LOCKED = true
        } else {
//            UntitledWorld.LOGGER.fatal("try genAttributeInstance while map is locked!")
            throw IllegalArgumentException("try genAttributeInstance while map is locked!")
        }
        return this
    }

    fun initializeData() {
        // TODO: read from CODEC
    }

    /**
     * basic cast logic, overrides should always call super.cast()
     * */
    open fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        helper.addCypherAttribute(ATTRIBUTE_MAP)
    }

    /***/
    open fun onCast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {}
}