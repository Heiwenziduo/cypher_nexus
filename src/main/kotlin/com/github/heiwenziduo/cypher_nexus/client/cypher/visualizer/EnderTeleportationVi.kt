package com.github.heiwenziduo.cypher_nexus.client.cypher.visualizer

import com.github.heiwenziduo.cypher_nexus.client.cypher.ICypherVisualizer
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.EnderTeleportationCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object EnderTeleportationVi : ICypherVisualizer.IBasicItemVisualizer {
    override fun cypher(): AbstractCypher = EnderTeleportationCypher
    override val stack = ItemStack(Items.ENDER_PEARL)

}