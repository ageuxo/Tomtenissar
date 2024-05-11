package io.github.ageuxo.TomteMod;

import io.github.ageuxo.TomteMod.datagen.ModBlockStateProvider;
import io.github.ageuxo.TomteMod.datagen.ModItemModelProvider;
import io.github.ageuxo.TomteMod.datagen.ModRecipeProvider;
import io.github.ageuxo.TomteMod.datagen.ModTagsProviders;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.TOMTE.get(), BaseTomte.createAttributes().build());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ModTagsProviders.addAll(generator, packOutput, lookup, fileHelper);
        generator.addProvider(true, new ModBlockStateProvider(packOutput, fileHelper));
        generator.addProvider(true, new ModItemModelProvider(packOutput, fileHelper));
        generator.addProvider(true, new ModRecipeProvider(packOutput));
    }
}
