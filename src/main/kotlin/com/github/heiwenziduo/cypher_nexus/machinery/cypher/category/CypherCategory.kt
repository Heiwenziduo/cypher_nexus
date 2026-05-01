package com.github.heiwenziduo.cypher_nexus.machinery.cypher.category

import com.github.heiwenziduo.cypher_nexus.utility.i.IRegisterable
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation

class CypherCategory(
    override val resource: ResourceLocation
): IRegisterable {
    /** lang-JSON key: cypher.category.{MOD_ID}.{category_name} */
    override fun translation(): MutableComponent =
        Component.translatable("cypher.category.${resource.namespace}.${resource.path}")
}