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
    open val manaDrain: Float = 0f
    open val draw: Int = 0
    protected val attributeMap = HashMap<Holder<CypherAttribute>, CypherAttributeInstance>()

    private var MAP_IS_LOCKED = false // this seems unnecessary

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
    protected fun addAttribute(attribute: Holder<CypherAttribute>): AbstractCypher {
        return addAttribute(attribute, CypherAttributeOperation.ADD, 0.0)
    }
    /**
     * add attribute with default value
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>, base: Double): AbstractCypher {
        return addAttribute(attribute, CypherAttributeOperation.BASE, base)
    }
    /**
     * */
    protected fun addAttribute(attribute: Holder<CypherAttribute>, operation: CypherAttributeOperation, value: Double): AbstractCypher {
        if (!MAP_IS_LOCKED)
            attributeMap.put(attribute, CypherAttributeInstance(attribute).addModifier(operation, value))
        else UntitledWorld.LOGGER.fatal("try add attribute ${attribute.registeredName} while map is locked!")
        return this
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
        helper.addAttribute(attributeMap)
    }

    /** custom logic up to subclasses */
    // TODO maybe change this to "hook"
    open fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper, wandLength: Float) {}

    // ============================================================================================================
    override fun toString(): String = resource.path

    // since cyphers are in the same registry, their names are unlikely to repeat,
    // but using category prefix can make it tidy
    private fun translationKey(): String = "cypher.${resource.namespace}.${category.value().registryName()}.${resource.path}"

    /**
     * lang-JSON key: cypher.{MOD_ID}.{cypher_category}.{cypher_name}?.{key}
     * @param key represents name if empty
     * */
    open fun translation(key: TranslationKey?): MutableComponent =
        Component.translatable("${translationKey()}${if (key==null) "" else ".$key"}")
    override fun translation(): MutableComponent = translation(null)

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