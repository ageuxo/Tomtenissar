package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;

public class MilkCowBehaviour extends DelayedBehaviour<BaseTomte> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.MILKING_STATION.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.CHORE_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT)
    );

    public MilkCowBehaviour() {
        super(10);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, BaseTomte entity) {
        LivingEntity livingEntity = BrainUtils.getMemory(entity, MemoryModuleType.INTERACTION_TARGET);
        if (livingEntity instanceof Cow cow) {
            return entity.closerThan(cow, 1.2D);
        }
        LOGGER.debug("extraConditions not met");
        return false;
    }

    @Override
    protected void start(BaseTomte entity) {
        entity.setStealing(true); // start animation
        entity.playSound(SoundEvents.GENERIC_DRINK);
        LOGGER.debug("starting milking");
        BrainUtils.setForgettableMemory(entity, ModMemoryTypes.CHORE_COOLDOWN.get(), true, 20);
    }

    @Override
    protected void doDelayedAction(BaseTomte entity) {
        LOGGER.debug("delayed action");
        BlockPos pos = BrainUtils.getMemory(entity, ModMemoryTypes.MILKING_STATION.get()).pos();
        BlockEntity be = entity.level().getBlockEntity(pos);
        LivingEntity livingEntity = BrainUtils.getMemory(entity, MemoryModuleType.INTERACTION_TARGET);
        if (be instanceof MilkingWorkStationBE milkStation && livingEntity instanceof Cow cow){
            milkStation.doAction(cow);
            entity.playSound(SoundEvents.COW_MILK);
            BrainUtils.setForgettableMemory(entity, ModMemoryTypes.CHORE_COOLDOWN.get(), true, 40);
            LOGGER.debug("milked and cooling down");
        }
    }

    @Override
    protected void stop(BaseTomte entity) {
        BrainUtils.clearMemory(entity, MemoryModuleType.INTERACTION_TARGET);
    }
}
