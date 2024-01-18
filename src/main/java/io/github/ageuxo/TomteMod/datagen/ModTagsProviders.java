package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModTagsProviders{

    public static void addAll(DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper fileHelper){
        var blockTags = generator.addProvider(true, new BlockTags(packOutput, lookup, fileHelper));
        generator.addProvider(true, new ItemTags(packOutput, lookup, blockTags.contentsGetter(), fileHelper));
    }

    public static class ItemTags extends ItemTagsProvider {

        public ItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, pBlockTags, TomteMod.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ModTags.STEALABLES).add(net.minecraft.world.item.Items.GOLD_INGOT, net.minecraft.world.item.Items.DIAMOND, net.minecraft.world.item.Items.COOKIE, net.minecraft.world.item.Items.EMERALD);
        }
    }

    public static class BlockTags extends BlockTagsProvider {

        public BlockTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, TomteMod.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ModTags.TOMTE_NOTEWORTHY).add(Blocks.CHEST, ModBlocks.SIMPLE_PRESENT.get());
        }
    }
}
