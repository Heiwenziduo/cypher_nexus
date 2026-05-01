package com.github.heiwenziduo.cypher_nexus.content.cypher

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.ModifierCypher
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder

/** easy way to create lots of simple modifiers */
class SimpleModifier(manaDrain: Float, path: String) : ModifierCypher(manaDrain) {
    override val resource = CypherNexus.modResource(path)
    fun attribute(attribute: Holder<CypherAttribute>, operator: CypherAttributeOperation, value: Double): SimpleModifier {
        return addAttribute(attribute, operator, value) as SimpleModifier
    }
}