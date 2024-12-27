package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.ModPoiTypes;
import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModTagsProviders{

    public static void addAll(DataGenerator generator, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup){
        var blockTags = generator.addProvider(true, new BlockTags(packOutput, lookup));
        generator.addProvider(true, new ItemTags(packOutput, lookup, blockTags.contentsGetter()));
        generator.addProvider(true, new PoiTags(packOutput, lookup));
    }

    public static class ItemTags extends ItemTagsProvider {

        public ItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
            super(pOutput, pLookupProvider, pBlockTags, TomteMod.MODID, null);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ModTags.STEALABLES).add(net.minecraft.world.item.Items.GOLD_INGOT, net.minecraft.world.item.Items.DIAMOND, net.minecraft.world.item.Items.COOKIE, net.minecraft.world.item.Items.EMERALD);
        }
    }

    public static class BlockTags extends BlockTagsProvider {

        public BlockTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider) {
            super(pOutput, pLookupProvider, TomteMod.MODID, null);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {

        }
    }

    public static class PoiTags extends TagsProvider<PoiType> {

        protected PoiTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider) {
            super(pOutput, Registries.POINT_OF_INTEREST_TYPE, pLookupProvider, TomteMod.MODID, null);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ModTags.WORK_STATIONS).add(ModPoiTypes.MILKING_STATION.getKey(), ModPoiTypes.SHEARING_STATION.getKey());
        }
    }
}
