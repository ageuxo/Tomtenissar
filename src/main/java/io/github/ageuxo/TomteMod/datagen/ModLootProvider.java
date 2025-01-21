package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.TomtePudding;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootProvider extends LootTableProvider{

    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModEntityLootProvider::new, LootContextParamSets.ENTITY)
                ),
                registries);
    }

    public static class ModBlockLootProvider extends BlockLootSubProvider {

        public ModBlockLootProvider(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(h->(Block)h.get()).toList();
        }

        @Override
        protected void generate() {
            add(ModBlocks.TOMTE_PUDDING.get(), createBooleanPropertyTable(ModBlocks.TOMTE_PUDDING.get(), TomtePudding.FILLED, true, LootItem.lootTableItem(Items.BOWL)));
            dropSelf(ModBlocks.SHEARING_WORK_STATION.get());
            dropSelf(ModBlocks.MILKING_WORK_STATION.get());
        }

        @SuppressWarnings("SameParameterValue")
        protected <T extends Block> LootTable.Builder createBooleanPropertyTable(T block, BooleanProperty property, boolean value, LootPoolEntryContainer.Builder<?> alternative) {
            return LootTable.lootTable()
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1.0F))
                                    .add(
                                            LootItem.lootTableItem(block)
                                                    .when(
                                                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                                            .hasProperty(property, value))
                                                    )
                                                    .otherwise(
                                                            alternative
                                                    )
                                    )
                    );
        }
    }

    public static class ModEntityLootProvider extends EntityLootSubProvider {

        protected ModEntityLootProvider(HolderLookup.Provider registries) {
            super(FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return ModEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
        }

        @Override
        public void generate() {
            add(ModEntities.TOMTE.get(),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1f))
                                            .add(
                                                    LootItem.lootTableItem(ModItems.YULE_GRAIN.get())
                                            )
                            )
                    );
        }
    }
}
