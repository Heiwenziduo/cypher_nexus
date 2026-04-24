package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeInstance
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.untitled_world.machinery.cypher.category.CypherCategory
import com.github.heiwenziduo.untitled_world.utility.i.IRegisterable
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

): IRegisterable {
    open val MANA_DRAIN: Float = 0f
    open val DRAW: Int = 0
    protected val ATTRIBUTE_MAP = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()
    private var MAP_IS_LOCKED = false

    abstract val category: Holder<CypherCategory>
    abstract override val resource: ResourceLocation

    init {
        initializeData()
        registerHooks()
        addAttribute(CypherAttributeRegistry.CAST_DELAY, CypherAttributeOperation.ADD, 0.0)
        addAttribute(CypherAttributeRegistry.RECHARGE_TIME, CypherAttributeOperation.ADD, 0.0)
    }

    /**
     * add attribute without default value
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>) {
        addAttribute(attribute, CypherAttributeOperation.ADD, 0.0)
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


    protected open fun initializeData() {
        // TODO: read from CODEC
    }

    /**
     * register a hook to modifier projectile-AI at specific moments
     * */
    protected open fun registerHooks() {}


    /**
     * basic cast logic
     * */
    fun cast(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper) {
        helper.addAttribute(ATTRIBUTE_MAP)
        // handle draw, mana_drain
    }

    /** custom logic up to subclasses */
    // TODO maybe change this to "hook"
    open fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper) {}

    // ============================================================================================================
    override fun toString(): String = resource.path

    private fun translationKey(): String = "cypher.${resource.namespace}.${category.value().registryName()}.${resource.path}"

    /** lang-JSON key: cypher.{MOD_ID}.{cypher_category}.{cypher_name}?.{key} */
    open fun translation(key: TranslationKey? = null): MutableComponent =
        Component.translatable("${translationKey()}${if (key==null) "" else ".$key"}")

    /** icons: {MOD_ID}/textures/cypher/{cypher_category}/{cypher_name}.png */
    open fun texture(): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath("${resource.namespace}",
            "textures/cypher/${category.value().registryName()}/${resource.path}.png")




    enum class TranslationKey() {
        DESCRIPTION,
        ;

        override fun toString(): String {
            return this.name.lowercase()
        }
    }
}