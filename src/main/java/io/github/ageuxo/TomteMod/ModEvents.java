package io.github.ageuxo.TomteMod;

import io.github.ageuxo.TomteMod.datagen.ModModelProviders;
import io.github.ageuxo.TomteMod.datagen.ModRecipeProvider;
import io.github.ageuxo.TomteMod.datagen.ModTagsProviders;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.concurrent.CompletableFuture;

public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.TOMTE.get(), BaseTomte.createAttributes().build());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        generator.addProvider(true, (DataProvider.Factory<ModModelProviders>) ModModelProviders::new);
        ModTagsProviders.addAll(generator, packOutput, lookup);
        generator.addProvider(true, new ModRecipeProvider.Runner(packOutput, lookup));
    }
}
