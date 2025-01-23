package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output , CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
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

        shaped(ModItems.RAW_PUDDING.asItem(), RecipeCategory.FOOD)
                .define('M', Items.MILK_BUCKET)
                .define('B', Items.BOWL)
                .define('G', ModItems.YULE_GRAIN)
                .pattern("G")
                .pattern("M")
                .pattern("B")
                .unlockedBy(getHasName(ModItems.YULE_GRAIN), has(ModItems.YULE_GRAIN))
                .save(output);

        simpleCookingRecipe(output, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new,
                600, ModItems.RAW_PUDDING, ModBlocks.TOMTE_PUDDING.asItem(), 1f);
        simpleCookingRecipe(output, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new,
                100, ModItems.RAW_PUDDING, ModBlocks.TOMTE_PUDDING.asItem(), 1f);
    }

    private @NotNull ShapedRecipeBuilder shaped(ItemLike result, RecipeCategory category) {
        return ShapedRecipeBuilder.shaped(category, result);
    }

    protected static <T extends AbstractCookingRecipe> void simpleCookingRecipe(
            RecipeOutput recipeOutput,
            String cookingMethod,
            RecipeSerializer<T> cookingSerializer,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            int cookingTime,
            ItemLike material,
            ItemLike result,
            float experience
    ) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(material), RecipeCategory.FOOD, result, experience, cookingTime, cookingSerializer, recipeFactory)
                .unlockedBy(getHasName(material), has(material))
                .save(recipeOutput, TomteMod.modRL(getItemName(result) + "_from_" + cookingMethod));
    }
}
