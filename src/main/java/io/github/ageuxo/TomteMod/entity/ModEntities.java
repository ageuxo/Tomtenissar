package io.github.ageuxo.TomteMod.entity;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TomteMod.MODID);

    public static final RegistryObject<EntityType<BaseTomte>> TOMTE = ENTITY_TYPES.register("tomte",
            ()->EntityType.Builder.of(BaseTomte::new, MobCategory.MISC)
                    .sized(0.5F, 0.75F)
                    .build("tomte"));

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
