package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SetWalkAndRummageTargetToInventory<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.NEARBY_BLOCK_ENTITIES.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
            Pair.of(ModMemoryTypes.RUMMAGE_TARGET.get(), MemoryStatus.VALUE_ABSENT));

    protected Pair<BlockPos, BlockEntityType<?>> target;
    protected BiPredicate<E, Pair<BlockPos, BlockEntityType<?>>> predicate = ((e, pair) -> {
        Optional<? extends BlockEntity> optional = e.level().getBlockEntity(pair.getFirst(), pair.getSecond());
        if (optional.isPresent()){
            BlockEntity be = optional.get();
            return be.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent();
        }
        return false;
    });
    protected BiFunction<E, Pair<BlockPos, BlockEntityType<?>>, Float> speedModFunction = (e, pair) -> 1f;
    protected BiFunction<E, Pair<BlockPos, BlockEntityType<?>>, Integer> closeEnoughFunction = (e, pair) -> 1;

    public SetWalkAndRummageTargetToInventory(){}


    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public SetWalkAndRummageTargetToInventory<E> predicate(BiPredicate<E, Pair<BlockPos, BlockEntityType<?>>> predicate) {
        this.predicate = predicate;

        return this;
    }

    public SetWalkAndRummageTargetToInventory<E> speedMod(BiFunction<E, Pair<BlockPos, BlockEntityType<?>>, Float> speedModifier) {
        this.speedModFunction = speedModifier;

        return this;
    }

    public SetWalkAndRummageTargetToInventory<E> closeEnoughWhen(BiFunction<E, Pair<BlockPos, BlockEntityType<?>>, Integer> function) {
        this.closeEnoughFunction = function;

        return this;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LOGGER.debug("Checking extra req. for SetWalkAndRummageTargetToInventory");
        List<Pair<BlockPos, BlockEntityType<?>>> memory = BrainUtils.getMemory(entity, ModMemoryTypes.NEARBY_BLOCK_ENTITIES.get());
        if (memory != null){
            for (Pair<BlockPos, BlockEntityType<?>> pos : memory) {
                if (this.predicate.test(entity, pos)){
                    this.target = pos;

                    break;
                }
            }
        }
        return this.target != null;
    }

    @Override
    protected void start(E entity) {
        LOGGER.debug("Setting rummage target to inventory at: {}", this.target.getFirst());
        BrainUtils.setMemory(entity, ModMemoryTypes.RUMMAGE_TARGET.get(), this.target);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.target.getFirst(), this.speedModFunction.apply(entity, this.target), this.closeEnoughFunction.apply(entity, this.target)));
        BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.target.getFirst()));
    }
}
