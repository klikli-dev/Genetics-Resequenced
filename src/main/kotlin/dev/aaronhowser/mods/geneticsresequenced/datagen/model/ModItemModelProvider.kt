package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, GeneticsResequenced.ID, existingFileHelper) {

    override fun registerModels() {
        simpleItem(ModItems.SCRAPER)
//        simpleItem(ModItems.SYRINGE)
//        simpleItem(ModItems.METAL_SYRINGE)
        simpleItem(ModItems.ORGANIC_MATTER)
        simpleItem(ModItems.CELL)
        simpleItem(ModItems.GMO_CELL)
        simpleItem(ModItems.DNA_HELIX)
        simpleItem(ModItems.PLASMID)
        simpleItem(ModItems.ANTI_PLASMID)
        simpleItem(ModItems.OVERCLOCKER)
        simpleItem(ModItems.ANTI_FIELD_ORB)
        simpleItem(ModItems.DRAGON_HEALTH_CRYSTAL)
//        simpleItem(ModItems.FRIENDLY_SLIME_SPAWN_EGG)

    }

    private fun simpleItem(item: DeferredItem<*>): ItemModelBuilder {
        return withExistingParent(
            item.id.path,
            ResourceLocation.withDefaultNamespace("item/generated") //Aiming at
        ).texture(
            "layer0",
            OtherUtil.modResource("item/${item.id.path}")
        )
    }

}