package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TomteMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEgg(ModItems.TOMTE_EGG);
    }

    private ItemModelBuilder spawnEgg(DeferredItem<? extends Item> item){
        return withExistingParent(item.getId().getPath(), ResourceLocation.withDefaultNamespace("item/template_spawn_egg"));
    }
}
