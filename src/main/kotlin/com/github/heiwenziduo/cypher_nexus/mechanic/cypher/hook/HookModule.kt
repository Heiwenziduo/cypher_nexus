package com.github.heiwenziduo.cypher_nexus.mechanic.cypher.hook

import com.github.heiwenziduo.cypher_nexus.utility.i.IRegisterable
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

class HookModule <HOOK : Any> (
    override val resource: ResourceLocation,
    val hook: KClass<HOOK>,
    val sync: Boolean = true,
    val target: HookTarget
): IRegisterable {

    enum class HookTarget {
        CASTING,
        PROJECTILE
    }
}