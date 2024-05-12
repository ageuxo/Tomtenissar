package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TomteMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.SHEARING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
        entityModel(ModBlocks.SHEARING_WORK_STATION);
        horizontalBlock(ModBlocks.MILKING_WORK_STATION.get(), models().getExistingFile(TomteMod.modRL("block/work_station")));
        entityModel(ModBlocks.MILKING_WORK_STATION);
    }

    public void entityModel(RegistryObject<? extends ItemLike> registryObject){
        itemModels().withExistingParent(registryObject.getId().getPath(), TomteMod.modRL("item/work_station"));
    }


}
