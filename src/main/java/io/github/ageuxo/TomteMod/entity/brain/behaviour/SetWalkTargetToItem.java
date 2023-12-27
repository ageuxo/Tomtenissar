package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SetWalkTargetToItem<E extends Mob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.IS_TEMPTED, MemoryStatus.REGISTERED)
    );

    protected BiPredicate<E, ItemEntity> predicate = (e, itemEntity) -> true;
    protected BiFunction<E, ItemEntity, Float> speedMod = ((e, itemEntity) -> 1f);
    protected BiFunction<E, ItemEntity, Integer> closeEnoughDistance = ((e, itemEntity) -> 0);

    protected ItemEntity target = null;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public SetWalkTargetToItem<E> setPredicate(BiPredicate<E, ItemEntity> predicate) {
        this.predicate = predicate;
        return this;
    }

    public SetWalkTargetToItem<E> setSpeedMod(BiFunction<E, ItemEntity, Float> speedMod) {
        this.speedMod = speedMod;
        return this;
    }

    public SetWalkTargetToItem<E> setCloseEnoughDistance(BiFunction<E, ItemEntity, Integer> closeEnoughDistance) {
        this.closeEnoughDistance = closeEnoughDistance;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        ItemEntity itemEntity = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
        if (this.predicate.test(entity, itemEntity) && entity.canHoldItem(itemEntity.getItem())){
            this.target = itemEntity;
        }
        return this.target == itemEntity;
    }

    @Override
    protected void start(E entity) {
        LOGGER.debug("target {}", this.target);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.target, this.speedMod.apply(entity, this.target), this.closeEnoughDistance.apply(entity, this.target)));
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, false));
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.IS_TEMPTED, true, entity.getRandom().nextInt(20, 60));
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }
}
