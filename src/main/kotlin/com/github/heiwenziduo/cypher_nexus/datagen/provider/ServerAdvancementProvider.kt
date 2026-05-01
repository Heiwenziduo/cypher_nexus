package com.github.heiwenziduo.cypher_nexus.datagen.provider

import com.github.heiwenziduo.cypher_nexus.datagen.generator.ServerAdvancementGenerator
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ServerAdvancementProvider(output: PackOutput,
                                registries: CompletableFuture<HolderLookup.Provider>,
                                existingFileHelper: ExistingFileHelper,
) : AdvancementProvider(output, registries, existingFileHelper,
    listOf(
        ServerAdvancementGenerator()
    )
) {

}