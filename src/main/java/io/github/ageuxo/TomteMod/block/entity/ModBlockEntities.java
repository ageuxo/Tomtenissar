package io.github.ageuxo.TomteMod.block.entity;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TomteMod.MODID);

    public static final RegistryObject<BlockEntityType<? extends ChestBlockEntity>> PRESENT_TYPE = BLOCK_ENTITIES.register("present_be", ()->BlockEntityType.Builder.of(PresentBlockEntity::new, ModBlocks.PRESENT.get()).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
