package com.github.heiwenziduo.untitled_world.content.cypher

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.machinery.cypher.BasicModifierCypher
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.untitled_world.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder

/** easy way to create lots of simple modifiers */
class SimpleModifier(manaDrain: Float, path: String) : BasicModifierCypher(manaDrain) {
    override val resource = UntitledWorld.modResource(path)
    fun attribute(attribute: Holder<CypherAttribute>, operation: CypherAttributeOperation, value: Double): SimpleModifier {
        return addAttribute(attribute, operation, value) as SimpleModifier
    }
}