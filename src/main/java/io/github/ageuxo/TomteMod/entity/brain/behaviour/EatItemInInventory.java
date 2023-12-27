package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.HeldBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;

public class EatItemInInventory<E extends BaseTomte> extends HeldBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(ModMemoryTypes.HAS_FOOD.get(), MemoryStatus.VALUE_PRESENT)
    );

    public EatItemInInventory() {
        onTick(entity -> {
            if (!entity.isUsingItem()) {
                LOGGER.debug("nextItem");
                entity.startUsingItem(InteractionHand.MAIN_HAND);
                entity.setEating(true);
            }
            return !entity.getHeldItem().isEmpty() && entity.getHeldItem().isEdible();
        });
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Boolean hasFood = BrainUtils.getMemory(entity, ModMemoryTypes.HAS_FOOD.get());
        if (entity.getHeldItem().isEmpty()){
            BrainUtils.clearMemory(entity, ModMemoryTypes.HAS_FOOD.get());
            return false;
        }
        LOGGER.debug("extraCheck: {}", hasFood != null && hasFood);
        return hasFood != null && hasFood;
    }

    @Override
    protected void start(E entity) {
        LOGGER.debug("start");
        entity.setEating(true);
        entity.startUsingItem(InteractionHand.MAIN_HAND);
        this.runFor(e -> e.getHeldItem().getUseDuration());
        BrainUtils.clearMemory(entity, ModMemoryTypes.HAS_FOOD.get());
    }

    @Override
    protected void tick(E entity) {
        if (!entity.isUsingItem()){
            LOGGER.debug("nextItem");
            entity.startUsingItem(InteractionHand.MAIN_HAND);
            entity.setEating(true);
        }
    }

    @Override
    protected void stop(E entity) {
        LOGGER.debug("stopping HP:{}", entity.getHealth());
        entity.setEating(false);
        entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1));

        if (entity.getHeldItem().isEmpty()){
            BrainUtils.clearMemory(entity, ModMemoryTypes.HAS_FOOD.get());
        }
    }
}
