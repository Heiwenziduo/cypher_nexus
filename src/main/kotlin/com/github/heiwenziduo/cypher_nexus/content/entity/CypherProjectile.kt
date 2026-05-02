package com.github.heiwenziduo.cypher_nexus.content.entity

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.ModEntities.CYPHER_PROJECTILE
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.EmptyCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import com.github.heiwenziduo.cypher_nexus.utility.VectorUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
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
import net.minecraft.world.phys.*
import kotlin.jvm.optionals.getOrNull


open class CypherProjectile(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level) {

    var cypher: AbstractCypher = EmptyCypher
    private var modifierList: List<AbstractCypher> = listOf()
    // private var _moveDireCache: Vec3? = null
    var moveDirection: Vec3 = Vec3.ZERO
//    var moveDirection: Vec3
//        get() {
//            val v3f = entityData.get(MOVE_DIRECTION)
//            return Vec3(v3f)
//        }
//        set(value) = entityData.set(MOVE_DIRECTION, value.toVector3f())
    var speed: Double
        get() = entityData.get(SPEED).toDouble()
        set(value) = entityData.set(SPEED, value.toFloat())

    var existing: Int
        get() = entityData.get(EXISTING)
        set(value) = entityData.set(EXISTING, value)
    var bounce: Int
        get() = entityData.get(BOUNCE)
        set(value) = entityData.set(BOUNCE, value)

    // should be immutable after initialization
    private val _attributeMap = HashMap<CypherAttribute, Double>()
    init {
        // initialize attribute, make sure every value is ready
        // since we have #getAttrOrDefault, this step seems unnecessary
//        CypherAttributeRegistry.REGISTRY.forEach {
//            if (it.isProjectileAttribute) _attributeMap.put(it, it.defaultValue)
//        }
    }

    constructor(level: Level, caster: LivingEntity?, cypher0: AbstractCypher, helper: CypherModifierHelper, direction: Vec3? = null) : this(CYPHER_PROJECTILE.get(), level) {
        // secondary constructor specific initialization
        owner = caster
        cypher = cypher0
        moveDirection = direction?: caster?.lookAngle?.normalize()?: moveDirection
        helper.computedOperationMap.forEach { (attr, helperMap) ->
            // do not modify the helper map here
            if (!attr.isProjectileAttribute) return@forEach
            _attributeMap.compute(attr) { a, v ->
                val def = helperMap.getOrDefault(CypherAttributeOperation.BASE, attr.defaultValue)
                val set = helperMap[CypherAttributeOperation.SET]
                val add = helperMap[CypherAttributeOperation.ADD]?: CypherAttributeOperation.ADD.defaultValue // same as Map#getOrDefault
                val mulBase = helperMap[CypherAttributeOperation.MULTIPLY_BASE]?: CypherAttributeOperation.MULTIPLY_BASE.defaultValue
                val mulTotal = helperMap[CypherAttributeOperation.MULTIPLY_TOTAL]?: CypherAttributeOperation.MULTIPLY_TOTAL.defaultValue
                // TODO restrict values in range
                val final = (set ?: ((def + add) * mulBase * mulTotal))

                final
            }
        }
        // sync attrs render-related
        existing = getAttrOrDefault(CypherAttributes.EXISTING).toInt()
        speed = getAttrOrDefault(CypherAttributes.SPEED)
        bounce = getAttrOrDefault(CypherAttributes.BOUNCE).toInt()

        prepareMotion()
        // test
        CypherNexus.LOGGER.info("create projectile $cypher")
        printModifiedAttrMap()
    }

    companion object {
//        val MOVE_DIRECTION: EntityDataAccessor<Vector3f> = SynchedEntityData.defineId(
//            CypherProjectile::class.java,
//            EntityDataSerializers.VECTOR3)
        val EXISTING: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
        val SPEED: EntityDataAccessor<Float> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.FLOAT)
        val BOUNCE: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
    }
    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
//        builder.define(MOVE_DIRECTION, Vector3f(0f, 0f, 0f))
        builder.define(EXISTING, 1)
        builder.define(SPEED, 0f)
        builder.define(BOUNCE, 0)
    }

    // ==================================================================================================================
    // ==================================================================================================================
    override fun tick() {
        // called on both server side and client side
        // prepareMotion()
        if (firstTick) {
            // start from tickCount == 1
            // hook

//            if (level().isClientSide){
//                println("tick$tickCount, clientSide speed: $speed") // attrs are synced from the start
//            }
        }
//        updateInWaterStateAndDoFluidPushing()
//        updateFluidOnEyes()
//        updateSwimming()
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
    /**
     * check hit-result and set delta-movement here
     * */
    private fun doProjectileTick() {
        /*
         * deltaMovement: the movement for the "next tick", client smooth animation relay on this
         * */
        val hitResult = // an AABB check is used everyTick every vanilla projectile, sounds outrageous, but is ok in performance
            ProjectileUtil.getHitResultOnMoveVector(this, { target: Entity -> canHitEntity(target) })
        // EventHooks.onProjectileImpact(this, hitResult), maybe get a result from broadcast
        if (hitResult.type != HitResult.Type.MISS) {
            hitTargetOrDeflectSelf(hitResult)
        }

        checkInsideBlocks() // trigger #onInsideBlock
        val vec3 = deltaMovement
        val d0 = x + vec3.x
        val d1 = y + vec3.y
        val d2 = z + vec3.z
        updateRotation()
        val f: Float = if (isInWater) 0.8f else 0.99f

        deltaMovement = vec3.scale(f.toDouble())
        applyGravity()
        setPos(d0, d1, d2) // #move do exactly the same with #setPos when dealing with "noPhysics"
    }


    // ==================================================================================================================
    // ==================================================================================================================
    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) { } // do nothing, don't call
    /**
     * since the projectile can't exist without a related cypher,
     * the #deltaMovement initialization will be done automatically, call #shoot is not necessary
     * */
    private fun prepareMotion() {
        // #getMovementToShoot
        if (moveDirection == Vec3.ZERO) deltaMovement = Vec3.ZERO

        deltaMovement = moveDirection.scale(speed)
    }

    override fun applyGravity() {
        // TODO hook, gravity
        var g0 = 0.01
        if (g0 != 0.0) deltaMovement = deltaMovement.add(0.0, -g0, 0.0)
    }


    override fun onHit(result: HitResult) {
        super.onHit(result)

        // bounce only when not pierce
        run bounce@ {
            if (bounce > 0) {
                bounce--
                val start = position()
                val targetBox = when(result){ // let's hope no one wants to extend HitResult, I assume a result is either entity or block
                    is EntityHitResult -> result.entity.boundingBox
                    is BlockHitResult -> AABB(result.blockPos)
                    else -> AABB(BlockPos(VectorUtil.toVec3i(result.location)))
                }
                // Direction.getRandom(random) // this will be fun, xd

                println("bounce check: ${if(level().isClientSide) "client" else "server"}")
                println("$start, $deltaMovement")

                val hitPoint = targetBox.clip(start, start.add(deltaMovement)) // TODO should store hitPoints, render them on client
                val direction = VectorUtil.getDireFromHit(hitPoint.getOrNull(), targetBox)
                if (direction == null) return@bounce "⑨"

                // bounce
                when(direction) {
                    // z axis represents NORTH&SOUTH
                    // FIXME this is crude, may not work when speed is high
                    Direction.DOWN, Direction.UP ->
                        deltaMovement = deltaMovement.multiply(1.0, -1.0, 1.0)
                    Direction.NORTH, Direction.SOUTH ->
                        deltaMovement = deltaMovement.multiply(1.0, 1.0, -1.0)
                    Direction.WEST, Direction.EAST ->
                        deltaMovement = deltaMovement.multiply(-1.0, 1.0, 1.0)
                }
                // TODO hook, bounce
                println("bounce!!! client: ${level().isClientSide}")
            }
        }

        if (!level().isClientSide) {
            if (bounce <= 0) {
                level().broadcastEntityEvent(this, 3) // combine with #handleEntityEvent
                discard()
            }
        }
    }
    override fun canHitEntity(target: Entity): Boolean {
        // TODO hook
        return super.canHitEntity(target)
    }
    override fun onHitEntity(result: EntityHitResult) {
        // TODO hook
        super.onHitEntity(result)
        val entity = result.entity
        val damage = getAttrOrDefault(CypherAttributes.DAMAGE)
        if (damage >= 0)
            entity.hurt(damageSources().thrown(this, this.owner), damage.toFloat())
    }
    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
    }


    // ==================================================================================================================
    // ==================================================================================================================
    override fun handlePortal() {
        // TODO
        super.handlePortal()
    }

    override fun handleEntityEvent(id: Byte) {
        // trigger on client
        super.handleEntityEvent(id)
        if (id.toInt() == 3) {
            for (i in 0..7) {
                // TODO
                this.level().addParticle(ParticleTypes.ITEM_SNOWBALL, x, y, z, 0.0, 0.0, 0.0)
            }
        }
        // check: ItemParticleOption(ParticleTypes.ITEM, itemstack), and ParticleTypes.ITEM_SNOWBALL
    }

    override fun shouldRenderAtSqrDistance(distance: Double): Boolean {
        // default distance based on AABB size, this is vital for very small entities
        // getViewScale()
        return distance <= 4096.0
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
    private fun printModifiedAttrMap() {
        _attributeMap.forEach { a, v ->
            println("$a: $v")
        }
    }
}