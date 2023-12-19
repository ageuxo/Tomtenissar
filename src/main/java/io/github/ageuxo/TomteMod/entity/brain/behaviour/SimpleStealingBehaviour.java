package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.slf4j.Logger;

import java.util.List;

public class SimpleStealingBehaviour<E extends BaseTomte> extends DelayedBehaviour<E> {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected BlockPos pos;
    protected int checkCooldown = 20;
    protected double minDistance = 1.73D;
    private long lastCheck;

    public static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.STEAL_TARGET.get(), MemoryStatus.VALUE_PRESENT)
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
        BrainUtils.clearMemory(entity, ModMemoryTypes.STEAL_TARGET.get());
        super.start(entity);
    }

    @Override
    protected void stop(E entity) {
        entity.setStealing(false);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if (level.getGameTime() - this.lastCheck < this.checkCooldown || level.getRandom().nextInt(2) != 1) {
            return false;
        } else if (entity.getMood() < 10){ //TODO tweak this
            LOGGER.debug("checkExtraStartConditions");
            this.pos = BrainUtils.getMemory(entity, ModMemoryTypes.STEAL_TARGET.get());
            this.lastCheck = level.getGameTime();
            return this.pos.closerToCenterThan(entity.position(), this.minDistance);
        }
        return false;
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        LOGGER.debug("shouldKeepRunning");
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
}
