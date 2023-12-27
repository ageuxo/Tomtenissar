package io.github.ageuxo.TomteMod.entity.brain;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.SBLForge;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModMemoryTypes {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(SBLForge.MEMORY_TYPES.getRegistryName(), TomteMod.MODID);

    public static final UnboundedMapCodec<BlockPos, Integer> VALUE_POS_CODEC = Codec.unboundedMap(BlockPos.CODEC, Codec.INT);
    public static final RegistryObject<MemoryModuleType<Map<BlockPos, Integer>>> ITEM_VALUE_POS = MEMORY_TYPES.register("item_value_pos", ()->new MemoryModuleType<>(Optional.of(VALUE_POS_CODEC)));

    @SuppressWarnings("deprecation")
    public static final Codec<List<Pair<BlockPos, BlockEntityType<?>>>> NEARBY_BLOCK_ENTITIES_CODEC = Codec.pair(BlockPos.CODEC, BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()).listOf();
    public static final RegistryObject<MemoryModuleType<List<Pair<BlockPos, BlockEntityType<?>>>>> NEARBY_BLOCK_ENTITIES = MEMORY_TYPES.register("nearby_block_entities", ()->new MemoryModuleType<>(Optional.of(NEARBY_BLOCK_ENTITIES_CODEC)));

    public static final RegistryObject<MemoryModuleType<BlockPos>> STEAL_TARGET = MEMORY_TYPES.register("steal_target", ()->new MemoryModuleType<>(Optional.of(BlockPos.CODEC)));

    public static final RegistryObject<MemoryModuleType<Pair<BlockPos, BlockEntityType<?>>>> RUMMAGE_TARGET = MEMORY_TYPES.register("rummage_target", ()->new MemoryModuleType<>(Optional.of(Codec.pair(BlockPos.CODEC, BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec()))));

    public static final RegistryObject<MemoryModuleType<Boolean>> HAS_FOOD = MEMORY_TYPES.register("has_food", ()->new MemoryModuleType<>(Optional.of(Codec.BOOL)));

    public static void register(IEventBus bus){
        MEMORY_TYPES.register(bus);
    }
}
