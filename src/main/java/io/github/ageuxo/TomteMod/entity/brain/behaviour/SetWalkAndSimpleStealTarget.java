package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.entity.MoodyMob;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SetWalkAndSimpleStealTarget<E extends Mob & MoodyMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SBLMemoryTypes.NEARBY_BLOCKS.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.STEAL_TARGET.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.PATH, MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.IS_TEMPTED, MemoryStatus.VALUE_ABSENT)
    );
    protected Pair<BlockPos, BlockState> target;
    protected BiPredicate<BlockPos, BlockState> predicate = (blockPos, state) -> state.is(Blocks.CHEST);
    protected BiFunction<E, Pair<BlockPos, BlockState>, Float> speedModFunction = (e, pair) -> 1f;
    protected BiFunction<E, Pair<BlockPos, BlockState>, Integer> closeEnoughFunction = (e, pair) -> 1;

    private final LinkedList<BlockPos> searchedPosCache = new LinkedList<>();

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (entity.getMood() < 0){
            var memory = BrainUtils.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
            //noinspection DataFlowIssue
            if (memory.isEmpty()) {
                BrainUtils.clearMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
                return false;
            } else {
                for (Pair<BlockPos, BlockState> pair : memory) {
                    if (this.predicate.test(pair.getFirst(), pair.getSecond()) && !this.searchedPosCache.contains(pair.getFirst())) {
                        this.target = pair;
                        addToCacheAndTrim(pair.getFirst());
                        LOGGER.debug("checkExtra cache:{}, {}, {}", searchedPosCache.size(), pair.getFirst(), pair.getSecond());
                        break;
                    }
                }
                if (this.target == null) {
                    LOGGER.debug("Found no valid pos");
                }
            }
        }
        return this.target != null;
    }

    public SetWalkAndSimpleStealTarget<E> setPredicate(BiPredicate<BlockPos, BlockState> predicate) {
        this.predicate = predicate;
        return this;
    }

    public SetWalkAndSimpleStealTarget<E> setSpeedModFunction(BiFunction<E, Pair<BlockPos, BlockState>, Float> speedModFunction) {
        this.speedModFunction = speedModFunction;
        return this;
    }

    public SetWalkAndSimpleStealTarget<E> setCloseEnoughFunction(BiFunction<E, Pair<BlockPos, BlockState>, Integer> closeEnoughFunction) {
        this.closeEnoughFunction = closeEnoughFunction;
        return this;
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setMemory(entity, ModMemoryTypes.STEAL_TARGET.get(), this.target.getFirst());
        BlockPosTracker posTracker = new BlockPosTracker(this.target.getFirst());
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(posTracker, this.speedModFunction.apply(entity, this.target), this.closeEnoughFunction.apply(entity, this.target)));
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, posTracker);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }

    private void addToCacheAndTrim(BlockPos pos){
        if (this.searchedPosCache.size() >= 5){
            this.searchedPosCache.removeFirst();
        }
        this.searchedPosCache.add(pos);
    }
}
