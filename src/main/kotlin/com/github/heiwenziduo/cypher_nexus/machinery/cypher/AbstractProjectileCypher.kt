package com.github.heiwenziduo.cypher_nexus.machinery.cypher

import com.github.heiwenziduo.cypher_nexus.content.entity.CypherProjectile
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

abstract class AbstractProjectileCypher: AbstractCypher() {
     fun createProjectile(
         level: Level, helper: CypherModifierHelper, caster: LivingEntity?,
         stack: ItemStack?, startPos: Vec3, direction: Vec3?, invokeList: List<AbstractCypher> = listOf()
     ) {
        val projectile = CypherProjectile(level, caster, this, helper, direction, invokeList)
        projectile.setPos(startPos)
        level.addFreshEntity(projectile)
    }
    final override fun addAttribute(holder: Holder<CypherAttribute>, base: Double): AbstractCypher = super.addAttribute(holder, base)

    fun getAttrBaseOrDefault(holder: Holder<CypherAttribute>) =
        attributeMap[holder]?.get(CypherAttributeOperation.BASE)?: holder.value().defaultValue
    fun getAttrBaseOrDefault(attr: CypherAttribute) = getAttrBaseOrDefault(attr.attrRegistryHolder())

}