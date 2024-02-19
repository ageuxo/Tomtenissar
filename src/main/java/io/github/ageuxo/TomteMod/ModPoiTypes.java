package io.github.ageuxo.TomteMod;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModPoiTypes {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, TomteMod.MODID);

    public static final RegistryObject<PoiType> MILKING_STATION = POI_TYPES.register("milking_station",
            ()-> new PoiType(getBlockstatesFiltered(state ->  true, ModBlocks.MILKING_WORK_STATION.get()), 0, 1));

    public static final RegistryObject<PoiType> SHEARING_STATION = POI_TYPES.register("shearing_station",
            ()-> new PoiType(getBlockstatesFiltered(state ->  true, ModBlocks.SHEARING_WORK_STATION.get()), 0, 1));

    @NotNull
    private static Set<BlockState> getBlockstatesFiltered(Predicate<BlockState> stateFilter, Block... blocks) {
        return Arrays.stream(blocks)
                .map(Block::getStateDefinition)
                .flatMap(definition -> definition.getPossibleStates().stream())
                .filter(stateFilter)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static void register(IEventBus bus){
        POI_TYPES.register(bus);
    }
}
