package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.ValidateNearbyPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValidateNearbyPoiBehaviour extends BuilderInstanceWrapperBehaviour<LivingEntity> {
    protected Predicate<Holder<PoiType>> poiFilter = poiTypeHolder -> true;
    protected MemoryModuleType<GlobalPos> poiPosMemory;
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> memoryRequirements;
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> DEFAULT_REQUIREMENTS = List.of();

    public ValidateNearbyPoiBehaviour(MemoryModuleType<GlobalPos> memory) {
        this.poiPosMemory = memory;
        this.memoryRequirements = List.of(
                Pair.of(memory, MemoryStatus.VALUE_PRESENT)
        );
    }

    protected Supplier<BehaviorControl<LivingEntity>> makeInstanceSupplier(Predicate<Holder<PoiType>> poiValidator, MemoryModuleType<GlobalPos> poiPosMemory){
        return ()-> ValidateNearbyPoi.create(poiValidator, poiPosMemory);
    }

    @Override
    protected void start(LivingEntity entity) {
        setInstanceSupplier(makeInstanceSupplier(this.poiFilter, this.poiPosMemory));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return DEFAULT_REQUIREMENTS;
    }

    public ValidateNearbyPoiBehaviour setPoiFilter(Predicate<Holder<PoiType>> poiValidator) {
        this.poiFilter = poiValidator;
        return this;
    }
}
