package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Cow;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;

public class FindMilkableBehaviour<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(ModMemoryTypes.MILKING_STATION.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT)
    );
    protected MilkingWorkStationBE milkingStation;

    public FindMilkableBehaviour(){
        this.cooldownProvider = entity -> 20;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        GlobalPos pos = BrainUtils.getMemory(entity, ModMemoryTypes.MILKING_STATION.get());
        if (pos.dimension() == entity.level().dimension()){
            if (level.getBlockEntity(pos.pos()) instanceof MilkingWorkStationBE milkingStationBE){
                if (milkingStationBE.canBeWorkedAt()){
                    this.milkingStation = milkingStationBE;
                    return true;
                } else {
                    LOGGER.debug("MilkingStation can't be worked at currently");
                }
            } else {
                LOGGER.debug("Position is not milking station: {}", pos.pos());
            }
        }
        BrainUtils.clearMemory(entity, ModMemoryTypes.MILKING_STATION.get());
        return false;
    }

    @Override
    protected void start(E entity) {
        List<Cow> cowList = this.milkingStation.getWorkableAnimals();
        if (!cowList.isEmpty()){
            Cow cow = cowList.get(entity.getRandom().nextInt(cowList.size()));
            BrainUtils.setMemory(entity, MemoryModuleType.INTERACTION_TARGET, cow);
        } else {
            LOGGER.debug("No valid target in cowList");
        }
    }
}
