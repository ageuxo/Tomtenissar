package io.github.ageuxo.TomteMod.entity;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomGameEventListener<E extends Entity> implements GameEventListener {
    protected final E e;
    protected final Consumer<E> callback;
    protected final Predicate<GameEvent.Context> filter;
    protected final PositionSource positionSource;
    protected final int listenerRadius;
    protected final Holder<GameEvent> gameEvent;

    public CustomGameEventListener(E entity, Holder<GameEvent> gameEvent, Consumer<E> callback, Predicate<GameEvent.Context> filter, int listenerRadius) {
        this.e = entity;
        this.gameEvent = gameEvent;
        this.callback = callback;
        this.filter = filter;
        this.positionSource = new EntityPositionSource(entity, entity.getEyeHeight());
        this.listenerRadius = listenerRadius;
    }

    @Override
    public PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        return this.listenerRadius;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (gameEvent.getKey() == this.gameEvent.getKey()) {
            if (this.filter.test(context)) {
                this.callback.accept(this.e);
                return true;
            }
        }
        return false;
    }
}
