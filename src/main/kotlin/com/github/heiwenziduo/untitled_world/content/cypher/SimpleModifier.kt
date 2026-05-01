package com.github.heiwenziduo.untitled_world.content.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.ModifierCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder

/** easy way to create lots of simple modifiers */
class SimpleModifier(manaDrain: Float, path: String) : ModifierCypher(manaDrain) {
    override val resource = UntitledWorld.modResource(path)
    fun attribute(attribute: Holder<CypherAttribute>, operator: CypherAttributeOperation, value: Double): SimpleModifier {
        return addAttribute(attribute, operator, value) as SimpleModifier
    }
}