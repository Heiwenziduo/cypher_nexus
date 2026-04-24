package com.github.heiwenziduo.untitled_world.machinery.cypher.hook

import com.github.heiwenziduo.untitled_world.utility.i.IRegisterable
import net.minecraft.resources.ResourceLocation

class CypherBehaviorHook(
    override val resource: ResourceLocation
): IRegisterable {
    // Q: how does the map on helper know what the hook the cypher exactly implement?
}