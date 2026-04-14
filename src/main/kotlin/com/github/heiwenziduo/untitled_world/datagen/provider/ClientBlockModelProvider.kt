package com.github.heiwenziduo.untitled_world.datagen.provider

import com.github.heiwenziduo.untitled_world.UntitledWorld
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ClientBlockModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    BlockModelProvider(output, UntitledWorld.MOD_ID, existingFileHelper
) {
    override fun registerModels() {

    }
}