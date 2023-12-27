package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.List;
import java.util.function.Predicate;

public class InteractWithDoorBehaviour extends BuilderInstanceWrapperBehaviour<LivingEntity> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.PATH, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED)
    );

    public InteractWithDoorBehaviour(){
        setInstanceSupplier(InteractWithDoor::create);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

}
