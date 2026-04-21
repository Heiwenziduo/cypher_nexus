package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 *
 * */
abstract class AbstractCypher(

) {
    open val MANA_DRAIN: Float = 0f
    open val DRAW: Int = 0
    protected val ATTRIBUTE_MAP = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()
    private var MAP_IS_LOCKED = false
    abstract val category: Holder<CypherCategory>

    init {
        initializeData()
        addAttribute(CypherAttributeRegistry.CAST_DELAY, CypherAttributeOperation.ADD, 0.0)
        addAttribute(CypherAttributeRegistry.RECHARGE_TIME, CypherAttributeOperation.ADD, 0.0)
    }

    /**
     * add attribute without default value
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(attribute, CypherAttributeInstance(attribute))
        else UntitledWorld.LOGGER.fatal("try add attribute ${attribute.registeredName} while map is locked!")
    }
    /**
     * add attribute with default value
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>, default: Double) {
        addAttribute(attribute, CypherAttributeOperation.BASE, default)
    }
    /**
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>, operation: CypherAttributeOperation, value: Double) {
        if (!MAP_IS_LOCKED)
            ATTRIBUTE_MAP.put(attribute, CypherAttributeInstance(attribute).addModifier(operation, value))
        else UntitledWorld.LOGGER.fatal("try add attribute ${attribute.registeredName} while map is locked!")
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

    abstract fun getResource(): ResourceLocation

    /**
     * basic cast logic
     * */
    fun cast(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper) {
        helper.addAttribute(ATTRIBUTE_MAP)
        // handle draw, mana_drain
    }

    /** custom logic up to subclasses */
    open fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper) {}

    // ============================================================================================================
    override fun toString(): String = getResource().path

    /** lang-JSON key: cypher.{MOD_ID}.{cypher_category}.{cypher_name} */
    open fun translation(): MutableComponent =
        Component.translatable("cypher.${getResource().namespace}.${category.value().registryName()}.${getResource().path}")

    /** icons: {MOD_ID}/textures/cypher/{cypher_category}/{cypher_name}.png */
    open fun texture(): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("${getResource().namespace}",
            "textures/cypher/${category.value().registryName()}/${getResource().path}.png")
}