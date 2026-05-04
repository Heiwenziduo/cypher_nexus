package com.github.heiwenziduo.cypher_nexus.machinery.cypher

import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttribute
import com.github.heiwenziduo.cypher_nexus.machinery.cypher.attribute.CypherAttributeOperation
import net.minecraft.core.Holder

abstract class AbstractNonProjectileCypher: AbstractCypher() {
    /**
     * add Attributes as its ADD value
     * */
    final override fun addAttribute(holder: Holder<CypherAttribute>, add: Double): AbstractCypher {
        return addAttribute(holder, CypherAttributeOperation.ADD, add)
    }

    // Do not add BASE value on NonProjectileCypher, which will not count
}