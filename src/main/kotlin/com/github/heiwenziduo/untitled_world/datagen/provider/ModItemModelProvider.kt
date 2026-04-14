package com.github.heiwenziduo.untitled_world.datagen.provider

import com.github.heiwenziduo.untitled_world.UntitledWorld
import com.github.heiwenziduo.untitled_world.init.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

// item models don't have an equivalent to blockstate files and are instead used directly
class ModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, UntitledWorld.MOD_ID, existingFileHelper
) {
    override fun registerModels() {
        // withExistingParent(ModItems.BASIC_WAND.toString(), mcLoc("item/handheld"))
        //     .texture("layer0", "item/basic_wand")
        // do the same things as the expression above
//        basicItem(ModItems.BASIC_WAND) // "item/generated"
        handheldItem(ModItems.BASIC_WAND) // "item/handheld"
    }
}