package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.ModLootTables;
import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGLMProvider extends GlobalLootModifierProvider {

    public ModGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, TomteMod.MODID);
    }

    @Override
    protected void start() {
        add(
                "mineshaft_additions",
                new AddTableLootModifier(new LootItemCondition[]{new LootTableIdCondition.Builder(BuiltInLootTables.ABANDONED_MINESHAFT.location()).build()},
                        ModLootTables.MINESHAFT_ADDITIONS)
        );
    }
}
