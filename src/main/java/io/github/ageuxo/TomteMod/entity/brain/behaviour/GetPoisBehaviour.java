package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetPoisBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> DEFAULT_REQUIREMENTS = List.of();
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> memoryRequirements = new ArrayList<>();
    protected Map<Holder<PoiType>, MemoryModuleType<GlobalPos>> poiToMemoryMap = new HashMap<>();
    protected Predicate<Holder<PoiType>> poiFilter = poiTypeHolder -> true;
    protected Predicate<BlockPos> posFilter = pos -> true;
    protected int maxDistance = 32;
    protected boolean runForExistingPois = true;

    public GetPoisBehaviour(){

    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return DEFAULT_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return runForExistingPois || poiToMemoryMap.values().stream().noneMatch(memoryType -> BrainUtils.hasMemory(entity, memoryType));
    }

    @Override
    protected void start(E entity) {
        PoiManager poiManager = ((ServerLevel) entity.level()).getPoiManager();
        for (Map.Entry<Holder<PoiType>, MemoryModuleType<GlobalPos>> entry : poiToMemoryMap.entrySet()){
            MemoryModuleType<GlobalPos> memory = entry.getValue();
            GlobalPos globalPos = BrainUtils.getMemory(entity, memory);
            if (globalPos != null && !poiManager.exists(globalPos.pos(), typeHolder -> typeHolder.is(entry.getKey().unwrapKey().orElseThrow()))) {
                BrainUtils.clearMemory(entity, memory);
            }
        }

        Stream<Pair<Holder<PoiType>, BlockPos>> pairStream = poiManager.findAllClosestFirstWithType(this.poiFilter, this.posFilter, entity.blockPosition(), this.maxDistance, PoiManager.Occupancy.ANY);
        var pairs = pairStream.collect(Collectors.toUnmodifiableSet());
        for (Pair<Holder<PoiType>, BlockPos> pair : pairs){
            MemoryModuleType<GlobalPos> memoryType = poiToMemoryMap.get(pair.getFirst());
            if (!BrainUtils.hasMemory(entity, memoryType)){
                BrainUtils.setMemory(entity, memoryType, GlobalPos.of(entity.level().dimension(), pair.getSecond()));
            }
        }
    }

    public GetPoisBehaviour<E> setPoiFilter(Predicate<Holder<PoiType>> poiFilter) {
        this.poiFilter = poiFilter;
        return this;
    }

    public GetPoisBehaviour<E> add(ResourceKey<PoiType> poiKey, MemoryModuleType<GlobalPos> memoryType){
        //noinspection deprecation
        this.poiToMemoryMap.put(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(poiKey), memoryType);
        this.memoryRequirements.add(Pair.of(memoryType, MemoryStatus.REGISTERED));
        return this;
    }

}
