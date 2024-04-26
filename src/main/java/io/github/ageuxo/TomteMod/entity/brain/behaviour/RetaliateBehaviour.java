package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class RetaliateBehaviour<E extends Mob> extends TargetOrRetaliate<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
        this.toTarget = BrainUtils.getMemory(owner, MemoryModuleType.HURT_BY_ENTITY);
        if (this.canAttackPredicate.test(this.toTarget)){
            if (this.alertAlliesPredicate.test(owner, this.toTarget)){
                alertAllies(level, owner);
            }
            return true;
        }
        return false;
    }

    public RetaliateBehaviour<E> attackablePredicate(Predicate<LivingEntity> predicate) {
        this.canAttackPredicate = predicate;

        return this;
    }

    public RetaliateBehaviour<E> useMemory(MemoryModuleType<? extends LivingEntity> memory) {
        throw new UnsupportedOperationException("Memory override not supported for RetaliateBehaviour");
    }

    public RetaliateBehaviour<E> alertAlliesWhen(BiPredicate<E, Entity> predicate) {
        this.alertAlliesPredicate = predicate;

        return this;
    }

    public RetaliateBehaviour<E> isAllyIf(BiPredicate<E, LivingEntity> predicate) {
        this.allyPredicate = predicate;

        return this;
    }
}
