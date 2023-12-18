package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RummageBehaviour<E extends BaseTomte> extends DelayedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int DELAY_TICKS = 260;
    protected LazyOptional<IItemHandler> target;
    protected BlockPos pos;

    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.RUMMAGE_TARGET.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(ModMemoryTypes.ITEM_VALUE_POS.get(), MemoryStatus.REGISTERED),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    public RummageBehaviour() {
        super(DELAY_TICKS);
        this.whenActivating(this::evaluateInventory);
        this.runFor((e -> e.getRandom().nextIntBetweenInclusive(30, 90)));
        this.cooldownFor(e -> e.getRandom().nextInt(30));
    }

    @Override
    protected void start(E entity) {
        LOGGER.debug("Starting rummage behaviour");
        //TODO start animation here
        entity.playSound(SoundEvent.createFixedRangeEvent(SoundEvents.CHEST_OPEN.getLocation(), 32));
        super.start(entity);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        LOGGER.debug("Checking extra req. for RummageBehaviour");
        Pair<BlockPos, BlockEntityType<?>> pair = BrainUtils.getMemory(entity, ModMemoryTypes.RUMMAGE_TARGET.get());
        if (pair == null) return false;
        Optional<? extends BlockEntity> optional = level.getBlockEntity(pair.getFirst(), pair.getSecond());
        if (pair.getFirst().getCenter().closerThan(entity.position(), 1.73D) && optional.isPresent()){
            BlockEntity blockEntity = optional.get();
            LazyOptional<IItemHandler> cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            this.target = cap;
            this.pos = pair.getFirst();
            return cap.isPresent();
        }
        return false;
    }

    public void evaluateInventory(E entity){
        if (this.target.isPresent()){
            Optional<IItemHandler> optional = this.target.resolve();
            if (optional.isPresent()){
                IItemHandler itemHandler = optional.get();
                int count = 0;
                for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                    ItemStack stack = itemHandler.getStackInSlot(slot);
                    if (stack.is(ModTags.STEALABLES)){
                        count += stack.getCount();
                    }
                }
                if (count > 0){
                    Map<BlockPos, Integer> valuePosMap;
                    if (BrainUtils.hasMemory(entity, ModMemoryTypes.ITEM_VALUE_POS.get())) {
                        valuePosMap = BrainUtils.getMemory(entity, ModMemoryTypes.ITEM_VALUE_POS.get());
                    } else {
                        valuePosMap = new Object2IntArrayMap<>();
                    }
                    valuePosMap.put(this.pos, count);
                    BrainUtils.setMemory(entity, ModMemoryTypes.ITEM_VALUE_POS.get(), valuePosMap);
                }
            }
        }
    }

    @Override
    protected void stop(E entity) {
        BrainUtils.clearMemory(entity, ModMemoryTypes.RUMMAGE_TARGET.get());
    }
}
