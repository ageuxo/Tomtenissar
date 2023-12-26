package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class InteractWithDoorBehaviour extends ExtendedBehaviour<LivingEntity> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.PATH, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.DOORS_TO_CLOSE, MemoryStatus.REGISTERED)
    );

    protected BehaviorControl<LivingEntity> instance;

    public InteractWithDoorBehaviour(){
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity entity) {
        this.instance = InteractWithDoor.create();
        return instance.tryStart(level, entity, level.getGameTime());
    }

    @Override
    protected void tick(LivingEntity entity) {
        instance.tickOrStop((ServerLevel) entity.level(), entity, entity.level().getGameTime());
    }

    @Override
    protected void stop(LivingEntity entity) {
        instance.doStop((ServerLevel) entity.level(), entity, entity.level().getGameTime());
    }
}
