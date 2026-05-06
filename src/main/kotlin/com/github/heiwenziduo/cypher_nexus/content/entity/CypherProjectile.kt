package com.github.heiwenziduo.cypher_nexus.content.entity

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.ModDataSerializer
import com.github.heiwenziduo.cypher_nexus.init.ModEntities.CYPHER_PROJECTILE
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherBehaviorHookRegistry.BEFORE_EXPIRE
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherBehaviorHookRegistry.FIRST_TICK
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherBehaviorHookRegistry.TICK_BEHAVIOR
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.AbstractProjectileCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.CypherModifierHelper
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.EmptyCypher
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.CypherFlags
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.flag.IFlaggable
import com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook.HookContainer
import com.github.heiwenziduo.cypher_nexus.utility.ProjectileUtility
import com.github.heiwenziduo.cypher_nexus.utility.VectorUtility
import com.github.heiwenziduo.cypher_nexus.utility.mod.CypherUtility
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.*
import kotlin.jvm.optionals.getOrNull


open class CypherProjectile(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level), IFlaggable {

    // var cypher: AbstractProjectileCypher = EmptyCypher
    // private var invokeList: List<AbstractCypher> = listOf()
    private var _cypher: AbstractProjectileCypher
        get() = entityData.get(CYPHER) as AbstractProjectileCypher
        set(value) = entityData.set(CYPHER, value)
    /** only modifier cyphers, check CypherModifierHelper#castLoop */
    private var _invokeList: List<AbstractCypher>
        get() = entityData.get(CYPHER_LIST)
        set(value) = entityData.set(CYPHER_LIST, value)

    val cypher
        get() = _cypher
    /***/
    val invokeList
        get() = _invokeList

    // private var _moveDireCache: Vec3? = null
    var moveDirection: Vec3 = Vec3.ZERO
    /** a flag is basically a bundle of booleans */
    override var enabledFlags: Int
        get() = entityData.get(FLAG)
        set(value) = entityData.set(FLAG, value)

//    var moveDirection: Vec3
//        get() {
//            val v3f = entityData.get(MOVE_DIRECTION)
//            return Vec3(v3f)
//        }
//        set(value) = entityData.set(MOVE_DIRECTION, value.toVector3f())
//    var speed: Double
//        get() = entityData.get(SPEED).toDouble()
//        set(value) = entityData.set(SPEED, value.toFloat())

    var existing: Int
        get() = entityData.get(EXISTING)
        set(value) = entityData.set(EXISTING, value)
    var bounce: Int
        get() = entityData.get(BOUNCE)
        set(value) = entityData.set(BOUNCE, value)


    // should be immutable after initialization
    private val _attributeMap = HashMap<CypherAttribute, Double>()
    /** store bounce points triggered in one tick */
    protected val bouncePoints = mutableListOf<Vec3>()
    protected val bounceTick
        get() = bouncePoints.isNotEmpty()
    val clipMargin = 0.2f
    val hookContainer = HookContainer()

    init {
        // initialize attribute, make sure every value is ready
        // since we have #getAttrOrDefault, this step seems unnecessary
//        CypherAttributeRegistry.REGISTRY.forEach {
//            if (it.isProjectileAttribute) _attributeMap.put(it, it.defaultValue)
//        }
    }

    constructor(
        level: Level, caster: LivingEntity?, cypher0: AbstractProjectileCypher, helper: CypherModifierHelper,
        direction: Vec3? = null, invokeList0: List<AbstractCypher> = listOf()
    ) : this(CYPHER_PROJECTILE.get(), level) {
        owner = caster
        _cypher = cypher0
        _invokeList = invokeList0
        enabledFlags = helper.enabledFlags
        syncHooks(_invokeList, _cypher)
        helper.computedOperationMap.forEach { (attr, opMap) ->
            // do not modify the helper map here
            if (!attr.isProjectileAttribute) return@forEach
            _attributeMap.compute(attr) { a, v ->
                val def = _cypher.getAttrBaseOrDefault(attr)
                val final = CypherUtility.attributeCalculator(opMap, def)
                attr.restrictRange(final)
            }
        }
        // sync attrs render-related
        existing = getAttrOrDefault(CypherAttributes.EXISTING).toInt()
//        speed = getAttrOrDefault(CypherAttributes.SPEED)
        bounce = getAttrOrDefault(CypherAttributes.BOUNCE).toInt()

        // prepareMotion
        moveDirection = direction?: caster?.lookAngle?.normalize()?: moveDirection
        if (moveDirection != Vec3.ZERO){
            deltaMovement = moveDirection.scale(getAttrOrDefault(CypherAttributes.SPEED))
            // if (caster != null) deltaMovement.add(caster.deltaMovement) // FIXME inertia behavior seems strange
        } else {
            deltaMovement = Vec3.ZERO
        }

        // test
        CypherNexus.LOGGER.info("create projectile $_cypher")
//        if (caster != null) println("caster move: ${caster.deltaMovement}")
        CypherFlags.printFlag(enabledFlags)
        printModifiedAttrMap()
    }

    companion object {
        val CYPHER: EntityDataAccessor<AbstractCypher> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            ModDataSerializer.CYPHER_DATA.get())
        val CYPHER_LIST: EntityDataAccessor<List<AbstractCypher>> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            ModDataSerializer.CYPHER_LIST_DATA.get())
        val FLAG: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
//        val MOVE_DIRECTION: EntityDataAccessor<Vector3f> = SynchedEntityData.defineId(
//            CypherProjectile::class.java,
//            EntityDataSerializers.VECTOR3)
//        val SPEED: EntityDataAccessor<Float> = SynchedEntityData.defineId(
//            CypherProjectile::class.java,
//            EntityDataSerializers.FLOAT)
        val EXISTING: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
        val BOUNCE: EntityDataAccessor<Int> = SynchedEntityData.defineId(
            CypherProjectile::class.java,
            EntityDataSerializers.INT)
    }
    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(CYPHER, EmptyCypher)
        builder.define(CYPHER_LIST, listOf())
        builder.define(FLAG, 0)
//        builder.define(MOVE_DIRECTION, Vector3f(0f, 0f, 0f))
//        builder.define(SPEED, 0f)
        builder.define(EXISTING, 1)
        builder.define(BOUNCE, 0)
    }

    private fun syncHooks(modifiers: List<AbstractCypher>, thiz: AbstractCypher? = null) {
        for (c in modifiers) {
            for (h in c.implementHooks) {
                hookContainer.add(h, c)
            }
        }
        if (thiz != null) {
            for (h in thiz.implementHooks) {
                hookContainer.add(h, thiz)
            }
        }
    }

    // ==================================================================================================================
    // ==================================================================================================================
    override fun tick() {
        // called on both server side and client side
        if (firstTick) { // start from tickCount == 1
            hookContainer.playHooks(FIRST_TICK)
            { h, i -> h.firstTickBoth(level(), this, i) }

            if (level().isClientSide) {
                println("firstTickCheckOnClient: _cypher, _invokeList\n$_cypher\n$_invokeList") // attrs are synced from the start
            }
        }

//        updateInWaterStateAndDoFluidPushing()
//        updateFluidOnEyes()
//        updateSwimming()
        super.tick() // TODO: prune default tick

        // hookContainer.get(TICK_BEHAVIOR).forEach { h, i -> h.tickBehaviorBoth(level(), this, i) }
        hookContainer.playHooks(TICK_BEHAVIOR)
        { h, i -> h.tickBehaviorBoth(level(), this, i) }
        projectileTick()
        modifierTick()
        if (existing == tickCount) {
            // here's a trick, if player make existing-time less or equal to 0, projectile will last till the game quit
            myDiscard(DiscardReason.EXPIRE)
        }
    }


    protected fun modifierTick() {}
    /**
     * check hit-result and set delta-movement here
     * */
    protected fun projectileTick() {
        /*
         * deltaMovement: the movement for the "next tick", client smooth animation relay on this
         * // an AABB check is used everyTick every vanilla projectile, sounds outrageous, but is ok in performance
         * */
        val hitResult = ProjectileUtility.getHitResult(position(), this, ::canHitEntity, deltaMovement, level(), clipMargin)
        bouncePoints.clear()
        val (lastBouncePoint, lastDeltaMove) = bounceLoop(hitResult)
        if (bounceTick) deltaMovement = VectorUtility.toSameDire(deltaMovement, lastDeltaMove)

//        run collideCheck@ {
//            val fluidCheck = ClipContext.Fluid.NONE // bounce when touching water surface?
//            val blockHit = level().clip(ClipContext(position(), deltaMovement, ClipContext.Block.COLLIDER, fluidCheck, this))
//        }

        checkInsideBlocks() // trigger #onInsideBlock

        updateRotation()

        applySpeedChange()
        applyGravity()

        // #move do exactly the same with #setPos when dealing with "noPhysics"
        if (bounceTick) setPos(lastBouncePoint.add(lastDeltaMove))
        else setPos(position().add(deltaMovement))
    }


    // ==================================================================================================================
    // ==================================================================================================================
    /**
     * since the projectile can't exist without a related cypher,
     * the #deltaMovement initialization will be done automatically, call #shoot is not necessary
     * */
    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) { } // do nothing, don't call

    override fun applyGravity() {
        // TODO hook, gravity
        val g0 = 0.01
        deltaMovement = deltaMovement.add(0.0, -g0, 0.0)
    }
    protected fun applySpeedChange() {
        // TODO hook, speed
        val f: Float = if (isInWater) 0.8f else 0.99f
        deltaMovement = deltaMovement.scale(f.toDouble())
    }

    /**
     * handle bounce movement logic and trigger #onHit.
     * @return a pair of lastHitPoint and deltaMove for the last leg, current #position and #deltaMovement if no bounce.
     * */
    private fun bounceLoop(hitResult: HitResult): Pair<Vec3, Vec3> {
        val defaultReturn = Pair(position(), deltaMovement)
        if (hitResult.type == HitResult.Type.MISS) return defaultReturn
        var hitResultStep = hitResult
        var startPosStep = position()
        var deltaMoveStep = deltaMovement

        do {
            // EventHooks.onProjectileImpact(this, hitResultStep), maybe get a result from broadcast
            if (!level().isClientSide) println("loop$bounce: \n$hitResultStep\n$startPosStep\n$deltaMoveStep")

            // FIXME image a situation that one proj with bounce can pierce block but can not pierce entity, it should bounce back when an entity stand behind a wall
            onHit(hitResultStep) // or hitTargetOrDeflectSelf(hitResult)
            val canPierce = hitResultStep is BlockHitResult && haveFlag(CypherFlags.PIERCE_BLOCK)
                    || hitResultStep is EntityHitResult && haveFlag(CypherFlags.PIERCE_ENTITY)
            if (bounce <= 0 || canPierce) break

            val targetBox = when(hitResultStep) {
                is EntityHitResult -> hitResultStep.entity.boundingBox.inflate(clipMargin.toDouble())
                is BlockHitResult -> AABB(hitResultStep.blockPos)
                else -> AABB(BlockPos(VectorUtility.toVec3i(hitResultStep.location)))
            }
            val hitPoint = targetBox.clip(startPosStep, startPosStep.add(deltaMoveStep)).getOrNull()

            if (!level().isClientSide) println("hitPoint $hitPoint \naabb $targetBox")

            val direction = VectorUtility.getDireFromHit(hitPoint, targetBox)
            if (hitPoint == null || direction == null) { // this block should not be reached
                if (!level().isClientSide) CypherNexus.LOGGER.fatal("hitPoint == null || direction == null\n$direction")
                return defaultReturn
            }

            // do reflect
            deltaMoveStep = hitPoint.vectorTo(startPosStep.add(deltaMoveStep))
            deltaMoveStep = when(direction) {
                Direction.DOWN, Direction.UP -> deltaMoveStep.multiply(1.0, -1.0, 1.0)
                Direction.NORTH, Direction.SOUTH -> deltaMoveStep.multiply(1.0, 1.0, -1.0)
                Direction.WEST, Direction.EAST -> deltaMoveStep.multiply(-1.0, 1.0, 1.0)
            }
            startPosStep = hitPoint
            bounce--
            bouncePoints.add(hitPoint)

            // handle next bounce
            hitResultStep = ProjectileUtility.getHitResult(startPosStep, this, ::canHitEntity, deltaMoveStep, level(), clipMargin)

        } while (hitResultStep.type != HitResult.Type.MISS)

        return Pair(startPosStep, deltaMoveStep)
    }

    override fun onHit(result: HitResult) {
        // distribute hitResult
        super.onHit(result)


        if (level().isClientSide) return
        val canPierce =
            result is BlockHitResult && haveFlag(CypherFlags.PIERCE_BLOCK) ||
            result is EntityHitResult && haveFlag(CypherFlags.PIERCE_ENTITY)
        if (!canPierce && bounce <= 0) {
            level().broadcastEntityEvent(this, 3) // combine with #handleEntityEvent
            myDiscard(DiscardReason.HIT)
        }
    }
    override fun canHitEntity(target: Entity): Boolean {
        if (!target.canBeHitByProjectile()) {
             return false // vanilla logic, for item-entities
        }
        if (haveFlag(CypherFlags.NO_DAMAGE)) return false
        if (owner == target && notHaveFlag(CypherFlags.HURT_OWNER)) return false
        // maybe hook
        return true
    }
    override fun onHitEntity(result: EntityHitResult) {
        // TODO hook
        super.onHitEntity(result)
        val entity = result.entity
        val damage = getAttrOrDefault(CypherAttributes.DAMAGE)
        if (notHaveFlag(CypherFlags.NO_DAMAGE))
            entity.hurt(damageSources().thrown(this, this.owner), damage.toFloat())
    }
    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
    }


    // ==================================================================================================================
    // ==================================================================================================================
    override fun handleEntityEvent(id: Byte) {
        // trigger on client
        super.handleEntityEvent(id)
        if (id.toInt() == 3) {
            _cypher.visualEffectOnHit(level(), this)
        }
    }

    override fun handlePortal() {
        // TODO
        super.handlePortal()
    }

    override fun shouldRender(x: Double, y: Double, z: Double): Boolean = super.shouldRender(x, y, z)
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
    fun getAttrOrDefault(attr: CypherAttribute): Double = _attributeMap[attr] ?: attr.defaultValue
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


    fun myDiscard(reason: DiscardReason) {
        when (reason) {
            DiscardReason.EXPIRE -> {
                hookContainer.playHooks(BEFORE_EXPIRE)
                { h, i -> h.beforeExpireBoth(level(), this, i) }
            }
            else -> {}
        }
        discard()
    }

    // ==================================================================================================================
    // ==================================================================================================================
    private fun printModifiedAttrMap() {
        _attributeMap.forEach { a, v ->
            println("$a: $v")
        }
    }


    // check Entity.RemovalReason for more info
    // here only for cypher-projectile usage
    enum class DiscardReason {
        /** reach its time limit */
        EXPIRE,
        /** through a collapse */
        HIT,
        HIT_ENTITY,
        HIT_BLOCK,

        /** by some special approach */
        ERASE,
    }
}