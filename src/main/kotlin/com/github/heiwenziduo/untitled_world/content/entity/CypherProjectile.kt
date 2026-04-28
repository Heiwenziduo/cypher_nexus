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
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3


open class CypherProjectile(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level) {

    var cypher: AbstractCypher = EmptyCypher
    private var modifierList: List<AbstractCypher> = listOf()
    var moveDirection: Vec3 = Vec3.ZERO
    var existing: Int
        get() = entityData.get(EXISTING)
        set(value) = entityData.set(EXISTING, value)
    var speed: Double
        get() = entityData.get(SPEED).toDouble()
        set(value) = entityData.set(SPEED, value.toFloat())

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
        existing = getAttrOrDefault(CypherAttributeRegistry.EXISTING).toInt()
        speed = getAttrOrDefault(CypherAttributeRegistry.SPEED)

        prepareMotion()
        // test
        UntitledWorld.LOGGER.info("create projectile $cypher")
        printAttrMap()
    }

    companion object {
        val EXISTING: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
        val SPEED: EntityDataAccessor<Float> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.FLOAT)
    }

    // ==================================================================================================================
    // ==================================================================================================================
    override fun tick() {
        // called on both server side and client side
        if (firstTick) {
            // start from tickCount == 1
            // hook
//            if (level().isClientSide){
//                println("tick$tickCount, clientSide speed: $speed")
//            }
        }
//        this.updateInWaterStateAndDoFluidPushing()
//        this.updateFluidOnEyes()
//        this.updateSwimming()
        super.tick() // TODO: prune default tick

        doModifierTick()
        doProjectileTick()
        if (existing == tickCount) {
            // here's a trick, if player make existing-time less or equal to 0, projectile will last till the game quit

            // hook, expire
            discard()
        }
    }


    private fun doModifierTick() {}
    private fun doProjectileTick() {
        val hitResult =
            ProjectileUtil.getHitResultOnMoveVector(this, { target: Entity -> this.canHitEntity(target) })
//        if (hitResult.type != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitResult)) {
//            this.hitTargetOrDeflectSelf(hitResult)
//        }
        if (hitResult.type != HitResult.Type.MISS) {
            this.hitTargetOrDeflectSelf(hitResult)
        }

        this.checkInsideBlocks()
        val vec3 = this.deltaMovement
        val d0 = this.x + vec3.x
        val d1 = this.y + vec3.y
        val d2 = this.z + vec3.z
        this.updateRotation()
        val f: Float
        if (this.isInWater) {
            f = 0.8f
        } else {
            f = 0.995f
        }

        this.deltaMovement = vec3.scale(f.toDouble())
        this.applyGravity()
        this.setPos(d0, d1, d2)
    }


    // ==================================================================================================================
    // ==================================================================================================================
    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        // super.shoot(x, y, z, velocity, inaccuracy)
    }
    /**
     * since the projectile can't exist without a related cypher,
     * the #deltaMovement initialization will be done automatically, call #shoot is not necessary
     * */
    private fun prepareMotion() {
        // #getMovementToShoot
        if (moveDirection == Vec3.ZERO) deltaMovement = Vec3.ZERO

        deltaMovement = moveDirection.scale(speed)
    }


    override fun onHit(result: HitResult) {
        super.onHit(result)
    }

    override fun canHitEntity(target: Entity): Boolean {
        // TODO
        return super.canHitEntity(target)
    }

    // ==================================================================================================================
    // ==================================================================================================================
    override fun handlePortal() {
        // TODO
        super.handlePortal()
    }

    override fun handleEntityEvent(id: Byte) {
        // trigger on client, maybe there is some special usage
        super.handleEntityEvent(id)
    }

    override fun shouldRenderAtSqrDistance(distance: Double): Boolean {
        // default distance based on AABB size, this is vital for very small entities
        // getViewScale()
        return distance <= 4096.0
    }

    override fun isNoGravity(): Boolean = false // #gravity is final, so change default
    override fun getDefaultGravity(): Double {
        // hook
        return 0.0
    }


    // ==================================================================================================================
    // ==================================================================================================================
    fun getAttribute(attr: CypherAttribute): Double? {
        return _attributeMap.get(attr)
    }
    fun getAttribute(holer: Holder<CypherAttribute>): Double? = getAttribute(holer.value())
    fun getAttrOrDefault(attr: CypherAttribute): Double = _attributeMap.get(attr)?: attr.defaultValue
    fun getAttrOrDefault(holer: Holder<CypherAttribute>): Double = getAttrOrDefault(holer.value())


    // ==================================================================================================================
    // ==================================================================================================================
    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(EXISTING, 1)
        builder.define(SPEED, 0f)
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
    // ==================================================================================================================
    private fun printAttrMap() {
        _attributeMap.forEach { a, v ->
            println("$a: $v")
        }
    }
}