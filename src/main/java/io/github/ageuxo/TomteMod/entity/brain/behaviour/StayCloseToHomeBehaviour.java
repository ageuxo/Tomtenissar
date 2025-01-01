package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StayCloseToHomeBehaviour<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    protected double homeRadius = 32;
    private BlockPos home;

    public StayCloseToHomeBehaviour(){

    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        GlobalPos memory = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
        if (memory.dimension().equals(entity.level().dimension()) && memory.pos().closerToCenterThan(entity.position(), this.homeRadius)) {
            this.home = memory.pos();
            return true;
        }
        return false;
    }

    @Override
    protected void start(E entity) {
        BlockPos pos = getPositionInRangeOfTarget(entity, this.home, (float) this.homeRadius);
        if (pos != null) {
            WalkTarget target = new WalkTarget(pos, 1.0f, 4);
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, target);
        }
    }

    protected StayCloseToHomeBehaviour<E> setHomeRadius(double homeRadius){
        this.homeRadius = homeRadius;
        return this;
    }

    @Nullable
    protected BlockPos getPositionInRangeOfTarget(E entity, BlockPos target, float range){
        Path path = entity.getNavigation().createPath(target, 1);
        if (path == null) return null;
        BlockPos ret = null;
        for (int i = 0; i < path.getNodeCount(); i++){
            Node node = path.getNode(i);
            if (node.distanceTo(target) < range){
                ret = node.asBlockPos().immutable();
                break;
            }
        }
        return ret;
    }
}
