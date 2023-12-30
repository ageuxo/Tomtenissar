package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.ModTags;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class SimpleStealingBehaviour<E extends BaseTomte> extends DelayedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected BlockPos pos;
    protected int checkCooldown = 20;
    protected int stealableMoodValue = 10;
    protected double minDistance = 1.73D;
    private long lastCheck;

    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.STEAL_TARGET.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.PATH, MemoryStatus.REGISTERED)
    );

    public SimpleStealingBehaviour() {
        super(7);
        this.runFor(e -> 15);
        this.cooldownFor(e -> e.getRandom().nextIntBetweenInclusive(30, 90));
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        LOGGER.debug("start");
        entity.setStealing(true);
        entity.playSound(SoundEvent.createFixedRangeEvent(SoundEvents.CHEST_OPEN.getLocation(), 32));
        super.start(entity);
    }

    @Override
    protected void doDelayedAction(E entity) {
        stealFromContainer(entity, this.pos);
    }

    @Override
    protected void stop(E entity) {
        entity.setStealing(false);
        BrainUtils.clearMemory(entity, ModMemoryTypes.STEAL_TARGET.get());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (level.getGameTime() - this.lastCheck < this.checkCooldown || level.getRandom().nextInt(2) != 1) {
            return false;
        } else if (entity.getMood() < 0){ //TODO tweak this
            LOGGER.debug("checkExtraStartConditions, mood:{}", entity.getMood());
            this.pos = BrainUtils.getMemory(entity, ModMemoryTypes.STEAL_TARGET.get());
            this.lastCheck = level.getGameTime();
            boolean closeEnough = this.pos.closerToCenterThan(entity.position(), this.minDistance);
            if (!closeEnough){
                BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(this.pos, 1f, 1));
            }
            return closeEnough;
        }
        return false;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.pos.closerToCenterThan(entity.position(), this.minDistance);
    }

    public SimpleStealingBehaviour<E> setCheckCooldown(int checkCooldown) {
        this.checkCooldown = checkCooldown;
        return this;
    }

    public SimpleStealingBehaviour<E> setMinDistance(double minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public void setStealableMoodValue(int stealableMoodValue) {
        this.stealableMoodValue = stealableMoodValue;
    }

    @SuppressWarnings("DataFlowIssue")
    public void stealFromContainer(E entity, BlockPos pos){
        LazyOptional<IItemHandler> lazyOptional = entity.level().getBlockEntity(pos).getCapability(ForgeCapabilities.ITEM_HANDLER);
        Optional<IItemHandler> optional = lazyOptional.resolve();
        if (optional.isPresent()){
            IItemHandler itemHandler = optional.get();
            for (int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (stack.is(ModTags.STEALABLES)){
                    int amount = Math.min(4, stack.getCount());
                    ItemStack stolen = itemHandler.extractItem(i, amount, true);
                    int simInserted = entity.itemHandler.insertItem(0, stolen, true).getCount();
                    int simStolen = stolen.getCount();
                    if (simInserted < simStolen){
                        LOGGER.debug("Stealing {}", stolen);
                        stolen = itemHandler.extractItem(i, amount, false);
                        entity.itemHandler.insertItem(0, stolen, false);
                        entity.addMood(stealableMoodValue);
                        break;
                    }
                }
            }
        }
    }
}
