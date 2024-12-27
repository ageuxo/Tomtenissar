package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.render.AnimalWorkstationSpecialRenderer;
import io.github.ageuxo.TomteMod.item.WorkStationItem;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ModModelProviders extends ModelProvider {

    public ModModelProviders(PackOutput output) {
        super(output, TomteMod.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        createWorkstationModel(blockModels, ModBlocks.SHEARING_WORK_STATION.get());
        createWorkstationModel(blockModels, ModBlocks.MILKING_WORK_STATION.get());

        registerSpecialItemRenderers(blockModels.itemModelOutput);
        itemModels.generateSpawnEgg(ModItems.TOMTE_EGG.get(), 0x520000, 0x1F4D27);
    }

    protected void registerSpecialItemRenderers(ItemModelOutput itemModelOutput) {
        itemModelOutput.accept(ModBlocks.SHEARING_WORK_STATION.asItem(),
                ItemModelUtils.specialModel(TomteMod.modRL("item/work_station"), new AnimalWorkstationSpecialRenderer.Unbaked(WorkStationItem.Type.SHEARING)));
        itemModelOutput.accept(ModBlocks.MILKING_WORK_STATION.asItem(),
                ItemModelUtils.specialModel(TomteMod.modRL("item/work_station"), new AnimalWorkstationSpecialRenderer.Unbaked(WorkStationItem.Type.MILKING)));
    }

    protected void createWorkstationModel(BlockModelGenerators blockModels, Block block) {
        ResourceLocation modelLocation = TomteMod.modRL("block/work_station");
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modelLocation))
                        .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

}
