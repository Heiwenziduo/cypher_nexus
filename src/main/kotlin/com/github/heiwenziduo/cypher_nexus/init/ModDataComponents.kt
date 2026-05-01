package com.github.heiwenziduo.cypher_nexus.init

import com.github.heiwenziduo.cypher_nexus.CypherNexus
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataFrequent
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataHighPayload
import com.github.heiwenziduo.cypher_nexus.machinery.wand.data.WandDataInvariable
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.util.function.Supplier


object ModDataComponents {
    val DEFERRED_REGISTER: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CypherNexus.MOD_ID)

    fun register() {
        DEFERRED_REGISTER.register(MOD_BUS)
    }

    val WAND_INVARIABLE: Supplier<DataComponentType<WandDataInvariable>> =
        DEFERRED_REGISTER.registerComponentType("wand_invariable")
        { it.persistent(WandDataInvariable.INVARIABLE_DATA_CODEC).networkSynchronized(WandDataInvariable.INVARIABLE_DATA_STREAM) }

    val WAND_HIGH_PAYLOAD: Supplier<DataComponentType<WandDataHighPayload>> =
        DEFERRED_REGISTER.registerComponentType("wand_high_payload")
        { it.persistent(WandDataHighPayload.HIGH_PAYLOAD_DATA_CODEC).networkSynchronized(WandDataHighPayload.HIGH_PAYLOAD_DATA_STREAM) }

    val WAND_FREQUENT: Supplier<DataComponentType<WandDataFrequent>> =
        DEFERRED_REGISTER.registerComponentType("wand_frequent")
        { it.persistent(WandDataFrequent.FREQUENT_DATA_CODEC).networkSynchronized(WandDataFrequent.FREQUENT_DATA_STREAM) }
}