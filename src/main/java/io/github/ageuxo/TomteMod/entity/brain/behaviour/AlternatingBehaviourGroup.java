package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public class AlternatingBehaviourGroup<E extends LivingEntity> extends BranchingBehaviourGroup<E> {
    protected boolean toggle;
    public AlternatingBehaviourGroup(ExtendedBehaviour<? super E> first, ExtendedBehaviour<? super E> second) {
        super(null, first, second);
        this.predicate = this::toggle;
    }

    private boolean toggle(E e){
        this.toggle = !this.toggle;
        return this.toggle;
    }
}
