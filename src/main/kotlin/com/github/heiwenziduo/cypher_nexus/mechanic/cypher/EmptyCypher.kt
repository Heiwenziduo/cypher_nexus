package com.github.heiwenziduo.cypher_nexus.mechanic.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.init.mod.CypherCategoryRegistry
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

/**
 * default registered cypher, like blocks:air, any cypher missing a registry name will be replaced with this.
 * */
object EmptyCypher: AbstractProjectileCypher() {
    override val draw = 1
    override val hide = true
    override val category = CypherCategoryRegistry.OTHER
    override val resource = CypherNexus.modResource("empty_cypher")
    init {

    }

    override fun createProjectile(
        level: Level,
        helper: CypherModifierHelper,
        caster: LivingEntity?,
        stack: ItemStack?,
        startPos: Vec3,
        direction: Vec3?,
        invokeList: List<AbstractCypher>
    ) {
        // do nothing
    }

    override fun onCastServer(
        level: Level,
        caster: LivingEntity?,
        stack: ItemStack?,
        helper: CypherModifierHelper,
        wandLength: Float
    ) {
        // do nothing
    }
}