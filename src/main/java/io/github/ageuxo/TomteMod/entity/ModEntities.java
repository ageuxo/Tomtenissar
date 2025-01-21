package io.github.ageuxo.TomteMod.entity;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TomteMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<BaseTomte>> TOMTE = ENTITY_TYPES.register("tomte",
            ()->EntityType.Builder.of(BaseTomte::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.75F)
                    .build("tomte"));

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
