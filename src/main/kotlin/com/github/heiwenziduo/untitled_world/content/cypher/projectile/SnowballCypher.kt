package com.github.heiwenziduo.untitled_world.content.cypher.projectile

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.entity.CypherProjectile
import com.github.heiwenziduo.untitled_world.init.mod.CypherAttributes
import com.github.heiwenziduo.untitled_world.machinery.cypher.BasicProjectileCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.CypherModifierHelper
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : BasicProjectileCypher(
    manaDrain = 20f
) {
    override val resource = UntitledWorld.modResource("snowball")

    init {
//        addAttribute(CypherAttributeRegistry.SPEED, 0.8)

        // test
        addAttribute(CypherAttributes.DAMAGE, 1.0)
        addAttribute(CypherAttributes.SPEED, 0.5)
        addAttribute(CypherAttributes.EXISTING, 200.0)
        addAttribute(CypherAttributes.BOUNCE, 5.0)

    }

    override fun onCastServer(level: Level, living: LivingEntity, stack: ItemStack?, helper: CypherModifierHelper, wandLength: Float) {
        super.onCastServer(level, living, stack, helper, wandLength)

        val castDire = living.lookAngle.normalize()
        // TODO consider wand tip inside an aabb
        val projPos = living.eyePosition.add(castDire.scale(wandLength.toDouble()))

        val snowball = CypherProjectile(level, living, this, helper, castDire)
        snowball.setPos(projPos)
        // snowball.shoot(castDire.x, castDire.y, castDire.z, 1f, 0f)
        level.addFreshEntity(snowball)

        // debug
        // helper.printComputedMap()
    }

}