package io.github.ageuxo.TomteMod.block.entity;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TomteMod.MODID);

    public static final RegistryObject<BlockEntityType<SimplePresentBlockEntity>> SIMPLE_PRESENT = BLOCK_ENTITIES.register("simple_present_be",
            ()-> BlockEntityType.Builder.of(SimplePresentBlockEntity::new, ModBlocks.SIMPLE_PRESENT.get()).build(null));
    public static final RegistryObject<BlockEntityType<SimpleWorkStationBlockEntity>> WORK_STATION = BLOCK_ENTITIES.register("work_station_be",
            ()-> BlockEntityType.Builder.of(SimpleWorkStationBlockEntity::new, ModBlocks.SHEARING_WORK_STATION.get(), ModBlocks.MILKING_WORK_STATION.get()).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
