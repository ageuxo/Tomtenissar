package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public abstract class AbstractSetWalkTargetBehaviour<E extends Mob, T extends Entity> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected BiPredicate<E, T> predicate = (e, t) -> true;
    protected BiFunction<E, T, Float> speedMod = ((e, t) -> 1f);
    protected BiFunction<E, T, Integer> closeEnoughDistance = ((e, t) -> 0);
    protected MemoryModuleType<T> memoryType;
    protected T target = null;

    public AbstractSetWalkTargetBehaviour(MemoryModuleType<T> memoryType){
        this.memoryType = memoryType;
    }

    public AbstractSetWalkTargetBehaviour<E, T> setPredicate(BiPredicate<E, T> predicate) {
        this.predicate = predicate;
        return this;
    }

    public AbstractSetWalkTargetBehaviour<E, T> setSpeedMod(BiFunction<E, T, Float> speedMod) {
        this.speedMod = speedMod;
        return this;
    }

    public AbstractSetWalkTargetBehaviour<E, T> setCloseEnoughDistance(BiFunction<E, T, Integer> closeEnoughDistance) {
        this.closeEnoughDistance = closeEnoughDistance;
        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        T target = BrainUtils.getMemory(entity, this.memoryType);
        if (target == null){
            BrainUtils.clearMemory(entity, this.memoryType);
            return false;
        }
        if (this.predicate.test(entity, target)) {
            this.target = target;
            return true;
        }
        return false;
    }

    @Override
    protected void start(E entity) {
        float distance = entity.distanceTo(this.target);
        LOGGER.debug("{} ,target {}", distance, this.target);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.target, this.speedMod.apply(entity, this.target), this.closeEnoughDistance.apply(entity, this.target)));
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(this.target, false));
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }
}
