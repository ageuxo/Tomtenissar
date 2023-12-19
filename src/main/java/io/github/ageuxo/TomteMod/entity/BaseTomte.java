package io.github.ageuxo.TomteMod.entity;

import io.github.ageuxo.TomteMod.entity.brain.behaviour.SetWalkAndSimpleStealTarget;
import io.github.ageuxo.TomteMod.entity.brain.behaviour.SimpleStealingBehaviour;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;

import java.util.List;
import java.util.Map;

public class BaseTomte extends PathfinderMob implements SmartBrainOwner<BaseTomte> {
    public static final String MOOD_NBT_KEY = "tomte_mood";
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STEALING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);

    private int mood = 0;

    public BaseTomte(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState stealAnimationState = new AnimationState();
    private int stealAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide){
            setupAnimationStates();
        }
    }

    private void setupAnimationStates(){
        // Idle
        if (this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = this.getRandom().nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        // Steal
        if (this.isStealing() && this.stealAnimationTimeout <= 0){
            this.stealAnimationTimeout = 15; // anim length in ticks
            this.stealAnimationState.start(this.tickCount);
        } else {
            --this.stealAnimationTimeout;
        }

        if (!this.isStealing()){
            stealAnimationState.stop();
        }

        // Attack
        if (this.isAttacking() && this.attackAnimationTimeout <= 0){
            this.attackAnimationTimeout = 15; // anim length in ticks
            this.attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isStealing()){
            attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float state;
        if (this.getPose() == Pose.STANDING){
            state = Math.min(pPartialTick * 0.6f, 1.0f);
        } else {
            state = 0.0f;
        }
        this.walkAnimation.update(state, 0.2f);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(STEALING, false);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<? extends ExtendedSensor<? extends BaseTomte>> getSensors() {
        return ObjectArrayList.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>(),
                new NearbyBlocksSensor<BaseTomte>()
                        .setRadius(3)
                        .setPredicate((state, entity) -> state.is(Blocks.CHEST))
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>().attackablePredicate(entity -> entity instanceof Enemy),
                        new AvoidEntity<>().avoiding(livingEntity -> livingEntity instanceof Player).noCloserThan(4.0F),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<BaseTomte>(5)
                        .attackInterval((tomte)->11)
                        .whenStarting(tomte -> tomte.setAttacking(true))
        );
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends BaseTomte>> getAdditionalTasks() {
        return Map.of(Activity.CORE,
            new BrainActivityGroup<BaseTomte>(Activity.CORE)
                .behaviours(
                        new SetWalkAndSimpleStealTarget<>(),
                        new SimpleStealingBehaviour<>()
                ));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(MOOD_NBT_KEY, mood);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.mood = pCompound.getInt(MOOD_NBT_KEY);
    }

    public int getMood() {
        return mood;
    }

    public void setAttacking(boolean attacking){
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking(){
        return this.entityData.get(ATTACKING);
    }

    public void setStealing(boolean stealing){
        this.entityData.set(STEALING, stealing);
    }

    public boolean isStealing(){
        return this.entityData.get(STEALING);
    }
}
