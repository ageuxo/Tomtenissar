package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(ModBlocks.MILKING_WORK_STATION.asItem(), RecipeCategory.MISC)
                .define('C', Blocks.CRAFTING_TABLE.asItem())
                .define('M', Items.MILK_BUCKET)
                .define('L', ItemTags.LOGS)
                .pattern("LML")
                .pattern("LCL")
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(output);

        shaped(ModBlocks.SHEARING_WORK_STATION.asItem(), RecipeCategory.MISC)
                .define('C', Blocks.CRAFTING_TABLE.asItem())
                .define('M', Items.SHEARS)
                .define('L', ItemTags.LOGS)
                .pattern("LML")
                .pattern("LCL")
                .unlockedBy(getHasName(Items.SHEARS), has(Items.SHEARS))
                .save(output);
    }

    private @NotNull ShapedRecipeBuilder shaped(ItemLike result, @SuppressWarnings("SameParameterValue") RecipeCategory category) {
        return ShapedRecipeBuilder.shaped(registries.lookupOrThrow(BuiltInRegistries.ITEM.key()), category, result);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "ModRecipes";
        }
    }
}
