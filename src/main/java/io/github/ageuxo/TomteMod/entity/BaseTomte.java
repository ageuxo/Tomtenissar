package io.github.ageuxo.TomteMod.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.ModPoiTypes;
import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.behaviour.*;
import io.github.ageuxo.TomteMod.entity.brain.sensor.DummyDoorSensor;
import io.github.ageuxo.TomteMod.item.ItemHelpers;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FleeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.*;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearestItemSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class BaseTomte extends PathfinderMob implements SmartBrainOwner<BaseTomte>, MoodyMob {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOOD_NBT_KEY = "tomte_mood";
    public static final String ORIGIN_KEY = "tomte_origin";
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STEALING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);

    public IItemHandler itemHandler = new EntityHandsInvWrapper(this);
    private int mood = 0;
    protected DynamicGameEventListener<CustomGameEventListener<BaseTomte>> animalDeathEventListener;

    public BaseTomte(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
        this.animalDeathEventListener = new DynamicGameEventListener<>(new CustomGameEventListener<>(this, GameEvent.ENTITY_DIE, (tomte) -> this.addMood(-2, true), BaseTomte::deathGameEventFilter, 16));
        this.setMaxUpStep(1.5f);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState stealAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();

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

        this.attackAnimationState.animateWhen(this.isAttacking(), tickCount);
        this.stealAnimationState.animateWhen(this.isStealing(), tickCount);
        this.eatAnimationState.animateWhen(this.isEating(), tickCount);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
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
        this.entityData.define(EATING, false);
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
                        .setRadius(7)
                        .setPredicate((state, entity) -> state.is(ModTags.TOMTE_NOTEWORTHY)),
                new DummyDoorSensor<>(),
                new NearestItemSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new InteractWithDoorBehaviour(),
                new LookAtTarget<>(),
                new EatItemInSlotBehaviour<>()
                        .setAnimationCallback(this::setEating)
                        .setFinishedCallback((entity, stack) -> this.onFoodEaten()),
                new StayCloseToHomeBehaviour<>(),
                new MoveToWalkTarget<>()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<? extends BaseTomte> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new RetaliateBehaviour<>()
                                .attackablePredicate(retaliateTargetPredicate()),
                        new SetAttackTarget<>()
                                .attackPredicate(this::huntEnemyTargetPredicate),
                        new AvoidEntity<>()
                                .avoiding(livingEntity -> livingEntity instanceof Player).noCloserThan(2.0F)
                                .stopCaringAfter(4.0F),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    private static Predicate<LivingEntity> retaliateTargetPredicate() {
        return entity -> entity.isAlive() && (!(entity instanceof Player player) || !player.isCreative());
    }

    private boolean huntEnemyTargetPredicate(LivingEntity entity) {
        GlobalPos memory = BrainUtils.getMemory(this, MemoryModuleType.HOME);
        return entity instanceof Enemy && !(entity instanceof Creeper)
                && (entity.distanceToSqr(memory.pos().getCenter()) < 32);
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getFightTasks() {
        //noinspection unchecked
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new FirstApplicableBehaviour<>(
                        new FleeTarget<>()
                                .speedModifier(1.2f)
                                .startCondition(pathfinderMob -> pathfinderMob.getHealth() < pathfinderMob.getMaxHealth() / 3),
                        new SetWalkTargetToAttackTarget<>()
                ),
                new AnimatableMeleeAttack<BaseTomte>(5)
                        .attackInterval((tomte)->11)
                        .whenStarting(tomte -> tomte.setAttacking(true))
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Activity, BrainActivityGroup<? extends BaseTomte>> getAdditionalTasks() {
        return Map.of(
                Activity.WORK, new BrainActivityGroup<BaseTomte>(Activity.WORK)
                .behaviours(
                        new AllApplicableBehaviours<>(
                                new GetPoisBehaviour<>()
                                        .setPoiFilter(type -> type.is(ModTags.WORK_STATIONS))
                                        .add(ModPoiTypes.MILKING_STATION.getKey(), ModMemoryTypes.MILKING_STATION.get())
                                        .add(ModPoiTypes.SHEARING_STATION.getKey(), ModMemoryTypes.SHEARING_STATION.get())
                                        .cooldownFor(pathfinderMob -> 20)
                        ),
                        new FirstApplicableBehaviour<>(
                                new SetWalkTargetToItem<>().setPredicate(this::shouldPickup),
                                new SetWalkAndSimpleStealTarget<>().cooldownFor(entity -> 30),
                                new SimpleStealingBehaviour<>(),

                                new FirstApplicableBehaviour<>( // Chores
                                        new FirstApplicableBehaviour<>( // Find chore TODO finish this
                                                new FindMilkableBehaviour<>().cooldownFor(entity -> 20),
                                                new FindShearableBehaviour<>().cooldownFor(entity -> 20)
                                        ),
                                        new AllApplicableBehaviours<>(
                                                new SetWalkTargetToInteractTarget<>(),
                                                new FirstApplicableBehaviour<>( // Do chore
                                                        new MilkCowBehaviour().cooldownFor(entity -> 20),
                                                        new ShearSheepBehaviour().cooldownFor(entity -> 20)
                                                )
                                        )
                                ), // END Chores

                                new SetRandomWalkTarget<>()
                                        .cooldownFor(pathfinderMob -> pathfinderMob.getRandom().nextIntBetweenInclusive(30, 120))
                        )
                ),
                Activity.REST, new BrainActivityGroup<BaseTomte>(Activity.REST)
                        .behaviours(
                                new ValidateNearbyPoiBehaviour(MemoryModuleType.HOME)
                                        .setPoiFilter(poiTypeHolder -> poiTypeHolder.is(PoiTypes.HOME))
                                        .cooldownFor(entity -> entity.getRandom().nextInt(20, 120)),
                                new SleepInBed(),
                                new SetWalkToHomeBehaviour(),
                                new RandomWalkInsideBehaviour()
                )
        );
    }

    @Override
    public @Nullable SmartBrainSchedule getSchedule() {
        SmartBrainSchedule schedule = new SmartBrainSchedule();
        schedule.doAt(0, entity -> this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY));
        schedule.activityAt(2000, Activity.WORK);
        schedule.doAt(2000, BaseTomte::equalizeMood);
        schedule.activityAt(12000, Activity.REST);
        return schedule;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getEntity();
        if (entity instanceof Player){
            this.addMood(-5, true);
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> pListenerConsumer) {
        if (this.level() instanceof ServerLevel serverLevel){
            pListenerConsumer.accept(this.animalDeathEventListener, serverLevel);
        }
    }

    @Override
    public PathNavigation getNavigation() {
        PathNavigation navigation1 = super.getNavigation();
        NodeEvaluator evaluator = navigation1.getNodeEvaluator();
        evaluator.setCanOpenDoors(true);
        evaluator.setCanWalkOverFences(true);
        return navigation1;
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

    @Override
    public boolean wantsToPickUp(ItemStack pStack) {
        return  ((pStack.is(ModTags.STEALABLES) || wantsToEat(pStack)) && canHoldItem(pStack));
    }

    @Override
    public boolean canHoldItem(ItemStack pStack) {
        if (!pStack.isEmpty()){
            ItemStack mainHandItem = this.getMainHandItem();
            ItemStack offHandItem = this.getOffhandItem();
            if (mainHandItem.isEmpty() || ItemHelpers.canStack(pStack, mainHandItem)){
                return true;
            } else return offHandItem.isEmpty() || ItemHelpers.canStack(pStack, offHandItem);
        }
        return false;
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getItem();
        if (stack.isEmpty()) return;
        InteractionHand hand = this.wantsToEat(stack) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        int deltaCount = ItemHelpers.canStackHowMany(stack, this.getItemInHand(hand));
        if (deltaCount > 0){
            this.setItemInHand(hand, ItemHandlerHelper.copyStackWithSize(stack, deltaCount));
            this.onItemPickup(pItemEntity);
            this.take(pItemEntity, deltaCount);
            stack.shrink(deltaCount);
        }
        if (stack.isEmpty()){
            pItemEntity.discard();
        }
    }

    @Override
    public void onItemPickup(ItemEntity pItemEntity) {
        super.onItemPickup(pItemEntity);
        ItemStack stack = pItemEntity.getItem();
        int mood = getMoodValueOfStack(stack);
        if (this.wantsToEat(stack)){
            BrainUtils.setMemory(this, ModMemoryTypes.HAS_FOOD.get(), true);
        }
        LOGGER.debug("addMood: {}", mood);
        this.addMood(mood, true);
    }

    public int getMoodValueOfStack(ItemStack stack) {
        int mood = 0;
        if (stack.is(ModTags.STEALABLES)) {
            mood = stack.getCount() * 10;
        } else {
            FoodProperties foodProperties = stack.getFoodProperties(this);
            if (foodProperties != null) {
                int foodValue = (int) ((foodProperties.getNutrition() * foodProperties.getSaturationModifier()) * stack.getCount());
                mood = foodValue * 4;
            }
        }
        return mood;
    }

    @Override
    public int getMood() {
        return mood;
    }

    @Override
    public void setMood(int mood) {
        this.mood = mood;
    }

    @Override
    public void addMood(int mood, boolean visible){
        this.mood += mood;
        Level level = this.level();
        if (visible && !level.isClientSide){
            byte eventId;
            if (mood >= 0){
                eventId = -68;
            } else {
                eventId = -69;
            }
            this.level().broadcastEntityEvent(this, eventId);
        }
    }

    protected void addParticlesAroundSelf(ParticleOptions particleOptions) {
        for(int i = 0; i < 5; ++i) {
            double xSpeed = this.random.nextGaussian() * 0.02D;
            double ySpeed = this.random.nextGaussian() * 0.02D;
            double zSpeed = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(particleOptions, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        switch (pId){
            case -67 -> addParticlesAroundSelf(ParticleTypes.HEART);
            case -68 -> addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
            case -69 -> addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
            default -> super.handleEntityEvent(pId);
        }
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

    public void setEating(boolean eating){
        this.entityData.set(EATING, eating);
    }

    public boolean isEating(){
        return this.entityData.get(EATING);
    }

    public boolean shouldPickup(LivingEntity entity, ItemEntity item){
        return (item.getItem().is(ModTags.STEALABLES) || this.wantsToEat(item.getItem())) && this.canHoldItem(item.getItem());
    }

    public boolean wantsToEat(ItemStack stack){
        if (stack.isEdible()) {
            FoodProperties food = stack.getFoodProperties(this);
            //noinspection DataFlowIssue
            for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                if (pair.getFirst().getEffect().getCategory() == MobEffectCategory.HARMFUL){
                    return false;
                }
            }
            return (food.getNutrition() * food.getSaturationModifier()) > 2f;
        }
        return false;
    }

    private static void equalizeMood(LivingEntity entity) {
        BaseTomte tomte = (BaseTomte) entity;
        if (tomte.getMood() > 15) {
            tomte.addMood(-2, false);
        } else if (tomte.getMood() < -15) {
            tomte.addMood(2, false);
        }
    }

    public static boolean deathGameEventFilter(GameEvent.Context context){
        return context.sourceEntity() instanceof Animal;
    }

    public void onFoodEaten(){
        this.addEffect(new MobEffectInstance(MobEffects.HEAL, 1));
        this.level().broadcastEntityEvent(this, (byte) -67);
    }

    @Override
    protected float getJumpPower() {
        return 0.50F * this.getBlockJumpFactor() + this.getJumpBoostPower();
    }
}
