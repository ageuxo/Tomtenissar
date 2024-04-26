package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.GroupBehaviour;
import net.tslat.smartbrainlib.object.SBLShufflingList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BranchingBehaviourGroup<E extends LivingEntity> extends GroupBehaviour<E> {
    protected Predicate<E> predicate;

    public BranchingBehaviourGroup(Predicate<E> predicate, ExtendedBehaviour<? super E> first, ExtendedBehaviour<? super E> second) {
        super(first, second);
        this.predicate = predicate;
    }

    @Override
    protected @Nullable ExtendedBehaviour<? super E> pickBehaviour(ServerLevel level, E entity, long gameTime, SBLShufflingList<ExtendedBehaviour<? super E>> extendedBehaviours) {
        var behaviour = extendedBehaviours.get(test(entity) ? 0 : 1);
        if (behaviour.tryStart(level, entity, gameTime)){
            return behaviour;
        } else {
            return null;
        }
    }

    public boolean test(E entity){
        return this.predicate.test(entity);
    }
}
