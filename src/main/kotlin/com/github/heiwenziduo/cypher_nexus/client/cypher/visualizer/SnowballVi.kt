package com.github.heiwenziduo.cypher_nexus.client.cypher.visualizer

import com.github.heiwenziduo.cypher_nexus.client.cypher.ICypherVisualizer
import com.github.heiwenziduo.cypher_nexus.content.cypher.projectile.SnowballCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.AbstractCypher
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object SnowballVi : ICypherVisualizer.IBasicItemVisualizer {
    override fun cypher(): AbstractCypher = SnowballCypher
    override val stack = ItemStack(Items.SNOWBALL)

}