package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TomteMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.SIMPLE_PRESENT.get(), models().getExistingFile(TomteMod.modRL("block/present/simple_present")));
        horizontalBlock(ModBlocks.SHEARING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
        simpleBlockItem(ModBlocks.SHEARING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
        horizontalBlock(ModBlocks.MILKING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
        simpleBlockItem(ModBlocks.MILKING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
    }


}
