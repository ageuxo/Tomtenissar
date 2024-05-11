package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MILKING_WORK_STATION.get())
                .define('C', Blocks.CRAFTING_TABLE.asItem())
                .define('M', Items.MILK_BUCKET)
                .define('L', ItemTags.LOGS)
                .pattern("LML")
                .pattern("LCL")
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SHEARING_WORK_STATION.get())
                .define('C', Blocks.CRAFTING_TABLE.asItem())
                .define('M', Items.SHEARS)
                .define('L', ItemTags.LOGS)
                .pattern("LML")
                .pattern("LCL")
                .unlockedBy(getHasName(Items.SHEARS), has(Items.SHEARS))
                .save(pWriter);
    }
}
