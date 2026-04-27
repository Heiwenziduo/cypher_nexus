package com.github.heiwenziduo.untitled_world.content.entity

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.ModEntities.CYPHER_PROJECTILE
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.IConsumerCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BasicCypherProjectileEntity(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level) {
    // lateinit var helper: CypherModifierHelper
    lateinit var cypher: AbstractCypher

    private val _attributeMap = HashMap<CypherAttribute, Double>()
    init {

    }
    constructor(level: Level, caster: LivingEntity?, cypher: AbstractCypher, helper: CypherModifierHelper) : this(CYPHER_PROJECTILE.get(), level) {
        // secondary constructor specific initialization
        this.owner = caster // kotaaaaalin?
        this.cypher = cypher
        if (cypher is IConsumerCypher) {
            cypher.attributeMap.forEach { (h, m) ->
                // do not modify the helper map here
                val attr = h.value()
                _attributeMap.compute(attr) { k, n ->
                    val def = m.getOrDefault(CypherAttributeOperation.BASE, attr.defaultValue)
                    val operMap = helper.computedMap.getOrElse(attr) { HashMap() }
                    val set = operMap[CypherAttributeOperation.SET]
                    val add = operMap[CypherAttributeOperation.ADD]?: CypherAttributeOperation.ADD.defaultValue
                    val mulBase = operMap[CypherAttributeOperation.MULTIPLY_BASE]?: CypherAttributeOperation.MULTIPLY_BASE.defaultValue
                    val mulTotal = operMap[CypherAttributeOperation.MULTIPLY_TOTAL]?: CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue
                    set ?: ((def + add) * mulBase * mulTotal)
                }
            }
        }

        UntitledWorld.LOGGER.info("create projectile $cypher")
        // test
        printAttrMap()
    }

    override fun tick() {
        super.tick()
    }

    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        super.shoot(x, y, z, velocity, inaccuracy)
    }



    // ==================================================================================================================

    fun getAttribute(attr: CypherAttribute): Double? {
        return _attributeMap.get(attr)
    }
    fun getAttribute(holer: Holder<CypherAttribute>): Double? = getAttribute(holer.value())

    // ==================================================================================================================

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
    }

    override fun getPickResult(): ItemStack? {
        // null by default, this is the creative mod middle button pick result
        return null
    }
    override fun isPickable(): Boolean {
        // false by default, entirely disable the picking activity
        return false
    }
    // a public method, to get a HitResult by checking if there is any block or entity in the direction of #getViewVector
    // ray cast
//    override fun pick(hitDistance: Double, partialTicks: Float, hitFluids: Boolean): HitResult {
//        return super.pick(hitDistance, partialTicks, hitFluids)
//    }

    // ==================================================================================================================
    private fun printAttrMap() {
        _attributeMap.forEach { a, v ->
            println("$a: $v")
        }
    }
}