package io.github.ageuxo.TomteMod;

import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.TOMTE.get(), BaseTomte.createAttributes().build());
    }
}
