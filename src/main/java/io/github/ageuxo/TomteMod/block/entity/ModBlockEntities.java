package io.github.ageuxo.TomteMod.block.entity;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import io.github.ageuxo.TomteMod.block.entity.workstations.ShearingWorkStationBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TomteMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MilkingWorkStationBE>> MILKING_STATION = BLOCK_ENTITIES.register("milking_station_be",
            ()-> new BlockEntityType<>(MilkingWorkStationBE::new, ModBlocks.MILKING_WORK_STATION.get()));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<ShearingWorkStationBE>> SHEARING_STATION = BLOCK_ENTITIES.register("shearing_station_be",
            ()-> new BlockEntityType<>(ShearingWorkStationBE::new, ModBlocks.SHEARING_WORK_STATION.get()));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
