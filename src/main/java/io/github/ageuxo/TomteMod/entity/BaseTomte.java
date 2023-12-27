package io.github.ageuxo.TomteMod.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import io.github.ageuxo.TomteMod.entity.brain.behaviour.*;
import io.github.ageuxo.TomteMod.entity.brain.sensor.DummyDoorSensor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.SleepInBed;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
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

public class BaseTomte extends PathfinderMob implements SmartBrainOwner<BaseTomte>, MoodyMob {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOOD_NBT_KEY = "tomte_mood";
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STEALING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(BaseTomte.class, EntityDataSerializers.BOOLEAN);

    public ItemStackHandler itemHandler = new ItemStackHandler(1);
    private int mood = 0;

    public BaseTomte(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
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
                new EatItemInInventory<>().runFor(tomte -> 23),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>().attackablePredicate(entity -> entity instanceof Enemy && !(entity instanceof Creeper)),
                        new AvoidEntity<>().avoiding(livingEntity -> livingEntity instanceof Player).noCloserThan(4.0F),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends BaseTomte> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new FleeTarget<>().startCondition(pathfinderMob -> pathfinderMob.getHealth() < pathfinderMob.getMaxHealth() / 3),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<BaseTomte>(5)
                        .attackInterval((tomte)->11)
                        .whenStarting(tomte -> tomte.setAttacking(true))
        );
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends BaseTomte>> getAdditionalTasks() {
        return Map.of(Activity.WORK,
            new BrainActivityGroup<BaseTomte>(Activity.WORK)
                .behaviours(
                        new FirstApplicableBehaviour<>(
                                new SetWalkTargetToItem<>().setPredicate(this::shouldPickup),
                                new SetWalkAndSimpleStealTarget<>().cooldownFor(entity -> 30),
                                new SimpleStealingBehaviour<>(),
                                new SetRandomWalkTarget<>().cooldownFor(pathfinderMob -> pathfinderMob.getRandom().nextIntBetweenInclusive(30, 120))
                        )
                ),
                Activity.REST, new BrainActivityGroup<BaseTomte>(Activity.REST)
                        .behaviours(
                                // eat food, regain mood per hunger value?
                                new FindAndValidatePoiBehaviour()
                                        .setPoiValidator(poiTypeHolder -> poiTypeHolder.is(PoiTypes.HOME))
                                        .setPoiPosMemory(MemoryModuleType.HOME),
                                new SleepInBed(),
                                new SetWalkToHomeBehaviour(),
                                new RandomWalkInsideBehaviour()
                ));
    }

    @Override
    public @Nullable SmartBrainSchedule getSchedule() {
        SmartBrainSchedule schedule = new SmartBrainSchedule();
        schedule.activityAt(2000, Activity.WORK);
        schedule.doAt(2000, BaseTomte::equalizeMood);
        schedule.doAt(2000, (entity)-> {
            BaseTomte tomte = (BaseTomte) entity;
            tomte.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        });
        schedule.activityAt(12000, Activity.REST);
        return schedule;
    }

    @Override
    public PathNavigation getNavigation() {
        PathNavigation navigation1 = super.getNavigation();
        navigation1.getNodeEvaluator().setCanOpenDoors(true);
        return navigation1;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt(MOOD_NBT_KEY, mood);
        pCompound.put("inventory", this.itemHandler.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.mood = pCompound.getInt(MOOD_NBT_KEY);
        this.itemHandler.deserializeNBT(pCompound.getCompound("inventory"));
    }

    @Override
    protected void dropEquipment() {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++){
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.isEmpty()){
                spawnAtLocation(stack);
                itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack pStack) {
        return  (pStack.is(ModTags.STEALABLES) || wantsToEat(pStack) && canHoldItem(pStack));
    }

    @Override
    public boolean canHoldItem(ItemStack pStack) {
        if (!pStack.isEmpty() && (pStack.is(ModTags.STEALABLES) || this.wantsToEat(pStack))){
            ItemStack remainder = itemHandler.insertItem(0, pStack, true);
            return remainder.isEmpty();
        }
        return false;
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getItem();
        ItemStack remainder = this.itemHandler.insertItem(0, stack.copy(), false);
        int deltaCount = stack.getCount() - remainder.getCount();
        if (remainder.getCount() < stack.getCount()){
            this.onItemPickup(pItemEntity);
            this.take(pItemEntity, deltaCount);
            stack.shrink(deltaCount);
            if (stack.isEmpty()){
                pItemEntity.discard();
            }
        }
    }

    @Override
    public ItemStack getItemInHand(InteractionHand pHand) {
        if (pHand == InteractionHand.MAIN_HAND){
            return getHeldItem();
        }
        return super.getItemInHand(pHand);
    }

    @Override
    public void onItemPickup(ItemEntity pItemEntity) {
        super.onItemPickup(pItemEntity);
        ItemStack stack = pItemEntity.getItem();
        int mood = getMoodValueOfStack(stack);
        if (stack.isEdible()){
            BrainUtils.setMemory(this, ModMemoryTypes.HAS_FOOD.get(), true);
        }
        LOGGER.debug("addMood: {}", mood);
        this.addMood(mood);
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
    public void addMood(int mood){
        this.mood += mood;
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
        return item.getItem().is(ModTags.STEALABLES) || this.wantsToEat(item.getItem());
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
            tomte.addMood(-2);
        } else if (tomte.getMood() < -15) {
            tomte.addMood(2);
        }
    }

    public ItemStack getHeldItem(){
        return this.itemHandler.getStackInSlot(0);
    }

    protected void clearInventory(){
        this.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

}
