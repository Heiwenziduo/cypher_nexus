package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributes
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
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
    private val _attributeMap = HashMap<Holder<CypherAttribute>, HashMap<CypherAttributeOperation, Double>>()
    val attributeMap
        get() = _attributeMap

    private var MAP_IS_LOCKED = false // this seems unnecessary

    abstract val category: Holder<CypherCategory>
    abstract override val resource: ResourceLocation

    init {
        initializeData()
        registerHooks()
        // TODO: these attrs only affect the process of casting, while others affect the projectile behaviors, should we separate them?
        addAttribute(CypherAttributes.CAST_DELAY, CypherAttributeOperation.ADD, 0.0)
        addAttribute(CypherAttributes.RECHARGE_TIME, CypherAttributeOperation.ADD, 0.0)
        addAttribute(CypherAttributes.RECOIL, CypherAttributeOperation.ADD, 0.0)
        addAttribute(CypherAttributes.SPREAD, CypherAttributeOperation.ADD, 0.0)
    }

    /**
     * add Attributes use #defaultValue as BASE value
     * */
    @Deprecated("no need to claim redundant #defaultValue")
    protected fun addAttribute(holder: Holder<CypherAttribute>): AbstractCypher {
        return addAttribute(holder, CypherAttributeOperation.BASE, null)
    }
    /**
     * add Attributes with its BASE value
     * */
    protected fun addAttribute(holder: Holder<CypherAttribute>, base: Double): AbstractCypher {
        return addAttribute(holder, CypherAttributeOperation.BASE, base)
    }
    /**
     * add Attributes with specific operator
     * */
    protected fun addAttribute(holder: Holder<CypherAttribute>, operator: CypherAttributeOperation, value: Double?): AbstractCypher {
        if (!MAP_IS_LOCKED) {
            val map = _attributeMap.getOrPut(holder) { HashMap() }
            if (value != null) map.compute(operator) { k,v -> operator.cumulate(v?: operator.defaultValue, value) }
        }
        else UntitledWorld.LOGGER.fatal("try add attribute ${holder.registeredName} while map is locked!")
        return this
    }
    /**
     * add Attributes to the helper
     * */
    fun addAttribute(helper: CypherModifierHelper) {
        helper.addAttribute(_attributeMap)
    }


    protected open fun initializeData() {
        // TODO: read from CODEC
    }

    /**
     * register a hook to modifier projectile-AI at specific moments
     * */
    protected open fun registerHooks() {}


    /** custom logic up to subclasses */
    // TODO maybe change this to "hook"
    open fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack?, helper: CypherModifierHelper, wandLength: Float) {}

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