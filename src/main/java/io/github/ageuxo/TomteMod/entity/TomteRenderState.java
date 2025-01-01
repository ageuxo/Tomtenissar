package io.github.ageuxo.TomteMod.entity;

import net.minecraft.world.entity.AnimationState;

public class TomteRenderState {
    public int idleAnimationTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState stealAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();

    public TomteRenderState() {
    }
}