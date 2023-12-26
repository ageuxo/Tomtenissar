package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class OpenDoorBehaviour<E extends Mob> extends ExtendedBehaviour<E> {
    protected ServerLevel level;
    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(
            Pair.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.level = level;
        return  (!entity.getNavigation().isDone() && entity.getNavigation().getPath() != null);
    }

    @Override
    protected void start(E entity) {
        openDoorIfNotCurrentlyOpen(this.level, entity, entity.blockPosition());
        openDoorIfNotCurrentlyOpen(this.level, entity, entity.getNavigation().getPath().getNextNode().asBlockPos());
    }

    public  static <E extends Mob> void openDoorIfNotCurrentlyOpen(ServerLevel level, E entity, BlockPos pos){
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.WOODEN_DOORS, blockState -> blockState.getBlock() instanceof DoorBlock)) {
            DoorBlock door = (DoorBlock) state.getBlock();
            if (!door.isOpen(state)){
                door.setOpen(entity, level, state, pos, true);
            }
        }
    }
}
