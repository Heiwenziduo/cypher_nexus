package com.github.heiwenziduo.untitled_world.content.entity

import com.github.heiwenziduo.untitled_world.init.ModEntities.CYPHER_PROJECTILE
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

open class BasicCypherProjectileEntity(entityType: EntityType<out Projectile>, level: Level) : Projectile(entityType, level) {
    lateinit var helper: CypherModifierHelper
    init {

    }
    constructor(level: Level, caster: LivingEntity?, helper: CypherModifierHelper) : this(CYPHER_PROJECTILE.get(), level) {
        // secondary constructor specific initialization
        owner = caster // kotaaaaalin?
        // FIXME should I duplicate the helper?
        this.helper = helper
    }

    override fun tick() {
        super.tick()
    }

    override fun shoot(x: Double, y: Double, z: Double, velocity: Float, inaccuracy: Float) {
        super.shoot(x, y, z, velocity, inaccuracy)
    }



    // ==================================================================================================================

    fun getAttribute(attr: Holder<CypherAttribute>): Double {
//        helper.
        return 0.0
    }

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
}