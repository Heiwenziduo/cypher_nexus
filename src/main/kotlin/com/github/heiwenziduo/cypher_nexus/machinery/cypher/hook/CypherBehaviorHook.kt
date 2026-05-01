package com.github.heiwenziduo.cypher_nexus.machinery.cypher.hook

import com.github.heiwenziduo.cypher_nexus.utility.i.IRegisterable
import net.minecraft.resources.ResourceLocation

class CypherBehaviorHook(
    override val resource: ResourceLocation
): IRegisterable {
    // Q: how does the map on helper know what the hook the cypher exactly implement?
}