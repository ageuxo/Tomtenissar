package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.HeldBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EatItemInSlotBehaviour<E extends LivingEntity> extends HeldBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(ModMemoryTypes.HAS_FOOD.get(), MemoryStatus.VALUE_PRESENT)
    );

    protected EquipmentSlot equipmentSlot = EquipmentSlot.MAINHAND;
    protected Consumer<Boolean> animationCallback = (doAnim) -> { };
    protected BiConsumer<E, ItemStack> finishedCallback = (e, itemStack) -> { };

    private ItemStack eatingStack;

    public EatItemInSlotBehaviour() {
    }

    public EatItemInSlotBehaviour<E> setAnimationCallback(Consumer<Boolean> animationCallback) {
        this.animationCallback = animationCallback;
        return this;
    }

    public EatItemInSlotBehaviour<E> setFinishedCallback(BiConsumer<E, ItemStack> finishedCallback) {
        this.finishedCallback = finishedCallback;
        return this;
    }

    public EatItemInSlotBehaviour<E> setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        ItemStack handItem = entity.getMainHandItem();
        if (handItem.isEmpty()){
            BrainUtils.clearMemory(entity, ModMemoryTypes.HAS_FOOD.get());
            return false;
        }
        LOGGER.debug("extraCheck: {}, edible:{}", handItem, handItem.isEdible());
        return handItem.isEdible() && !entity.isUsingItem();
    }

    @Override
    protected void start(E entity) {
        entity.startUsingItem(InteractionHand.MAIN_HAND);
        this.animationCallback.accept(true);
        this.eatingStack = entity.getItemBySlot(this.equipmentSlot);
        this.runFor(e -> e.getItemBySlot(this.equipmentSlot).getUseDuration());
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return entity.isUsingItem();
    }

    @Override
    protected void stop(E entity) {
        this.animationCallback.accept(false);
        this.finishedCallback.accept(entity, this.eatingStack.copy());
        this.eatingStack = null;
        LOGGER.debug("finished");
        if (entity.getItemBySlot(this.equipmentSlot).isEmpty()){
            BrainUtils.clearMemory(entity, ModMemoryTypes.HAS_FOOD.get());
        }
    }
}
