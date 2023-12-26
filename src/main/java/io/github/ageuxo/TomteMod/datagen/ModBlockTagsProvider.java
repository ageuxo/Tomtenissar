package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, TomteMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.TOMTE_NOTEWORTHY).add(Blocks.CHEST, ModBlocks.SIMPLE_PRESENT.get());
    }
}
