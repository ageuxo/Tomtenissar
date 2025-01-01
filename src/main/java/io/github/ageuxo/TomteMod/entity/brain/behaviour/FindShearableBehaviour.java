package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.block.entity.workstations.ShearingWorkStationBE;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Sheep;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;

public class FindShearableBehaviour<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(ModMemoryTypes.SHEARING_STATION.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT)
    );
    protected ShearingWorkStationBE shearingStation;

    public FindShearableBehaviour(){
        this.cooldownProvider = entity -> 20;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        GlobalPos pos = BrainUtils.getMemory(entity, ModMemoryTypes.SHEARING_STATION.get());
        if (pos.dimension() == entity.level().dimension()){
            if (level.getBlockEntity(pos.pos()) instanceof ShearingWorkStationBE shearingWorkStationBE){
                if (shearingWorkStationBE.canBeWorkedAt()){
                    this.shearingStation = shearingWorkStationBE;
                    return true;
                } else {
                    LOGGER.trace( "ShearingStation can't be worked at currently");
                }
            } else {
                LOGGER.trace( "Position is not shearing station: {}", pos.pos());
            }
        }
        BrainUtils.clearMemory(entity, ModMemoryTypes.SHEARING_STATION.get());
        return false;
    }

    @Override
    protected void start(E entity) {
        List<Sheep> sheepList = this.shearingStation.getWorkableAnimals();
        if (!sheepList.isEmpty()){
            Sheep sheep = sheepList.get(entity.getRandom().nextInt(sheepList.size()));
            BrainUtils.setMemory(entity, MemoryModuleType.INTERACTION_TARGET, sheep);
        } else {
            LOGGER.trace( "No valid target in sheepList");
        }
    }
}
