package com.github.heiwenziduo.untitled_world.content.cypher.modifier

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.content.hook.IProjectileTickHook
import com.github.heiwenziduo.untitled_world.machinery.cypher.BasicProjectileCypher
import net.minecraft.resources.ResourceLocation

object HomingCypher: BasicProjectileCypher(
    MANA_DRAIN = 50f
), IProjectileTickHook {
    override val resource: ResourceLocation = UntitledWorld.modResource("homing")

    override fun onProjectileTick() {
        TODO("Not yet implemented")
    }
}