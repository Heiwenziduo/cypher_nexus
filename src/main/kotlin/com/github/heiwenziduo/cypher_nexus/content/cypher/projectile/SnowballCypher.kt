package com.github.heiwenziduo.cypher_nexus.content.cypher.projectile

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherAttributes
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.ProjectileCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.CypherModifierHelper
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object SnowballCypher : ProjectileCypher(
    manaDrain = 10f
) {
    override val resource = CypherNexus.modResource("snowball")

    init {
//        addAttribute(CypherAttributeRegistry.SPEED, 0.8)

        // test
        addAttribute(CypherAttributes.DAMAGE, 1.0)
        addAttribute(CypherAttributes.SPEED, 0.3)
        addAttribute(CypherAttributes.EXISTING, 200.0)
        addAttribute(CypherAttributes.BOUNCE, 5.0)

    }

    override fun onCastServer(level: Level, caster: LivingEntity, stack: ItemStack?, helper: CypherModifierHelper, wandLength: Float) {
        super.onCastServer(level, caster, stack, helper, wandLength)

        val castDire = caster.lookAngle.normalize()
        // TODO consider wand tip inside an aabb
        val projPos = caster.eyePosition.add(castDire.scale(wandLength.toDouble()))

        val snowball = CypherProjectile(level, caster, this, helper, castDire)
        snowball.setPos(projPos)
        level.addFreshEntity(snowball)

        // debug
        // helper.printComputedMap()
    }

}