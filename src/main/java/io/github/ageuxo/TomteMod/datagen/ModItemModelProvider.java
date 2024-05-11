package io.github.ageuxo.TomteMod.datagen;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TomteMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        var test = spawnEgg(ModItems.TOMTE_EGG);
        LogUtils.getLogger().info(test.toString());
    }

    private ItemModelBuilder spawnEgg(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(), new ResourceLocation("minecraft:item/template_spawn_egg"));
    }
}
