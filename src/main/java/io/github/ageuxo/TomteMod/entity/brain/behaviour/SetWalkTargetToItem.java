package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class SetWalkTargetToItem<E extends Mob> extends AbstractSetWalkTargetBehaviour<E, ItemEntity> {
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.IS_TEMPTED, MemoryStatus.VALUE_ABSENT)
    );

    public SetWalkTargetToItem(){
        super(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.IS_TEMPTED, true, entity.getRandom().nextInt(20, 60));
    }
}
