package com.github.heiwenziduo.untitled_world.machinery.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld

/** dynamically define a modifier, use to handle wand specialty */
class TemporaryModifierCypher : BasicModifierCypher(
    manaDrain = 0f
) {
    override val resource = UntitledWorld.modResource("temporary_modifier")
}