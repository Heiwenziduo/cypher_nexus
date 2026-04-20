package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import net.minecraft.core.Holder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 *
 * */
abstract class AbstractCypher(

) {
    /**
     * property-map marks every property this specific cypher (-type) may have.
     * base value is not contained, which may be injected using JSON, or determine whether it calculates
     * */
    protected val ATTRIBUTE_MAP = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()
    private var MAP_IS_LOCKED = false
    // TODO
    val CATEGORY = 0

    init {
        initializeData()
        addAttribute(CypherAttributeRegistry.MANA_DRAIN)
        addAttribute(CypherAttributeRegistry.CAST_DELAY)
        addAttribute(CypherAttributeRegistry.RECHARGE_TIME)
        addAttribute(CypherAttributeRegistry.DRAW)
    }

    /**
     * Add a "key" to the map, its value needs to be filled via {#genAttributeInstance} manually.
     * This defines what attribute is available on the specific cypher
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>) {
        addAttribute(attribute, 0.0)
    }
    /**
     * add attribute with default value
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>, default: Double) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(attribute, CypherAttributeInstance(attribute).withDefault(default))
        else UntitledWorld.LOGGER.fatal("try add property ${attribute.registeredName} while map is locked!")
    }

    /**
     * should be called after any subclass initialization
     * TODO: Can it be managed here centrally?
     * */
    fun genAttributeInstance(): AbstractCypher {
        if (!MAP_IS_LOCKED) {
            ATTRIBUTE_MAP.forEach{(attr, instanceMaybeNull) ->
                ATTRIBUTE_MAP[attr] = CypherAttributeInstance(attr)
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
     * basic cast logic
     * */
    fun cast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {
        helper.addAttribute(ATTRIBUTE_MAP)
    }

    /** custom logic leave to subclasses */
    open fun onCast(level: Level, player: Player, stack: ItemStack, helper: CypherModifierHelper) {}
}