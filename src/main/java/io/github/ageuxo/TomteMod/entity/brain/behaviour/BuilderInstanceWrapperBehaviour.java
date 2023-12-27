package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.function.Supplier;

public abstract class BuilderInstanceWrapperBehaviour<E extends LivingEntity> extends ExtendedBehaviour<E> {
    protected BehaviorControl<E> instance;
    protected Supplier<BehaviorControl<E>> instanceSupplier;

    @Override
    protected void start(ServerLevel level, E entity, long gameTime) {
        super.start(level, entity, gameTime);
        this.instance = instanceSupplier.get();
        instance.tryStart(level, entity, gameTime);
    }

    @Override
    protected void tick(ServerLevel level, E entity, long gameTime) {
        instance.tickOrStop((ServerLevel) entity.level(), entity, entity.level().getGameTime());
        super.tick(level, entity, gameTime);
    }

    @Override
    protected void stop(ServerLevel level, E entity, long gameTime) {
        instance.doStop((ServerLevel) entity.level(), entity, entity.level().getGameTime());
        super.stop(level, entity, gameTime);
    }

    protected BuilderInstanceWrapperBehaviour<E> setInstanceSupplier(Supplier<BehaviorControl<E>> instanceSupplier){
        this.instanceSupplier = instanceSupplier;
        return this;
    }
}
