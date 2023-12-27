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

public class FindAndValidatePoiBehaviour extends BuilderInstanceWrapperBehaviour<LivingEntity> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.HOME, MemoryStatus.REGISTERED)
    );
    protected Predicate<Holder<PoiType>> poiValidator = poiTypeHolder -> true;
    protected MemoryModuleType<GlobalPos> poiPosMemory;

    public FindAndValidatePoiBehaviour() {

    }

    protected Supplier<BehaviorControl<LivingEntity>> makeInstanceSupplier(Predicate<Holder<PoiType>> poiValidator, MemoryModuleType<GlobalPos> poiPosMemory){
        return ()-> ValidateNearbyPoi.create(poiValidator, poiPosMemory);
    }

    @Override
    protected void start(LivingEntity entity) {
        setInstanceSupplier(makeInstanceSupplier(this.poiValidator, this.poiPosMemory));

    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public FindAndValidatePoiBehaviour setPoiValidator(Predicate<Holder<PoiType>> poiValidator) {
        this.poiValidator = poiValidator;
        return this;
    }

    public FindAndValidatePoiBehaviour setPoiPosMemory(MemoryModuleType<GlobalPos> poiPosMemory) {
        this.poiPosMemory = poiPosMemory;
        return this;
    }
}
