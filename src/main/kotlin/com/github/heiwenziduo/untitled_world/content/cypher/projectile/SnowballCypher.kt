package com.github.heiwenziduo.untitled_world.content.cypher.projectile

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributeRegistry
import com.github.heiwenziduo.untitled_world.machinery.cypher.BasicProjectileCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Snowball
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : BasicProjectileCypher(
    MANA_DRAIN = 20f
) {
    override fun getResource() = UntitledWorld.modResource("snowball")

    init {
        addAttribute(CypherAttributeRegistry.DAMAGE, 0.0)
        addAttribute(CypherAttributeRegistry.SPEED, 1.0)
        addAttribute(CypherAttributeRegistry.SPREAD, 1.0)

//
//        genAttributeInstance()
//        readAttributeFromDataList()
    }

    override fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack, helper: CypherModifierHelper) {
        super.onCastServer(level, living, stack, helper)

        val snowball = Snowball(level, living)
        snowball.setPos(living.position())
        snowball.shoot(living.lookAngle.x, living.lookAngle.y, living.lookAngle.z, 1f, 0f)
        level.addFreshEntity(snowball)

        // debug
        helper.peekComputedMap()
    }

}