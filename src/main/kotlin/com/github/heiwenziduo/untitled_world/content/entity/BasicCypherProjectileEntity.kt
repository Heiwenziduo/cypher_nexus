package com.github.heiwenziduo.untitled_world.content.entity

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.ModEntities.CYPHER_PROJECTILE
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.EmptyCypher
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
import net.minecraft.world.phys.Vec3

open class BasicCypherProjectileEntity(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level) {

    var cypher: AbstractCypher = EmptyCypher
    var moveDirection: Vec3 = Vec3.ZERO
    private var _existing: Int = 0
    private var _speed: Double = 0.0

    // should be immutable after initialization
    private val _attributeMap = HashMap<CypherAttribute, Double>()
    init {
        // initialize attribute, make sure every value is ready
        CypherAttributeRegistry.REGISTRY.forEach {
            if (it.isProjectileAttribute) _attributeMap.put(it, it.defaultValue)
        }
    }

    constructor(level: Level, caster: LivingEntity?, cypher: AbstractCypher, helper: CypherModifierHelper, direction: Vec3? = null) : this(CYPHER_PROJECTILE.get(), level) {
        // secondary constructor specific initialization
        this.owner = caster // kotaaaaalin?
        this.cypher = cypher
        this.moveDirection = direction?: caster?.lookAngle?.normalize()?: moveDirection

        if (cypher is IConsumerCypher) {
            cypher.attributeMap.forEach { (holder, map) ->
                // do not modify the helper map here
                val attr = holder.value()
                if (attr.isProjectileAttribute) // return in loop ends the function, pitfall
                _attributeMap.compute(attr) { k, n ->
                    val def = map.getOrDefault(CypherAttributeOperation.BASE, attr.defaultValue)
                    val operMap = helper.computedMap.getOrElse(attr) { HashMap() }
                    val set = operMap[CypherAttributeOperation.SET]
                    val add = operMap[CypherAttributeOperation.ADD]?: CypherAttributeOperation.ADD.defaultValue
                    val mulBase = operMap[CypherAttributeOperation.MULTIPLY_BASE]?: CypherAttributeOperation.MULTIPLY_BASE.defaultValue
                    val mulTotal = operMap[CypherAttributeOperation.MULTIPLY_TOTAL]?: CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue
                    // TODO restrict values in range
                    val final = (set ?: ((def + add) * mulBase * mulTotal))

                    final
                }
            }
        }
        _existing = getAttrOrDefault(CypherAttributeRegistry.EXISTING).toInt()
        _speed = getAttrOrDefault(CypherAttributeRegistry.SPEED)

        // test
        UntitledWorld.LOGGER.info("create projectile $cypher")
         printAttrMap()
    }

    override fun tick() {
        // called on both server side and client side
        if (firstTick) {
            // hook,
            print("firstTick, $tickCount")
        }
        modifierTick()

        moveTo(position().add(moveDirection.scale(_speed)))


//        this.updateInWaterStateAndDoFluidPushing()
//        this.updateFluidOnEyes()
//        this.updateSwimming()
        super.tick()
        if (_existing == tickCount) {
            // hook, expire
            // discard()
        }
    }

    private fun modifierTick() {}

    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        super.shoot(x, y, z, velocity, inaccuracy)
    }


    // TODO
    override fun handlePortal() {
        super.handlePortal()
    }

    // ==================================================================================================================

    fun getAttribute(attr: CypherAttribute): Double? {
        return _attributeMap.get(attr)
    }
    fun getAttribute(holer: Holder<CypherAttribute>): Double? = getAttribute(holer.value())
    fun getAttrOrDefault(attr: CypherAttribute): Double = _attributeMap.get(attr)?: attr.defaultValue
    fun getAttrOrDefault(holer: Holder<CypherAttribute>): Double = getAttrOrDefault(holer.value())

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