package io.github.ageuxo.TomteMod.entity.brain.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.brain.ModMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class StealBehaviour<E extends BaseTomte> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(ModMemoryTypes.ITEM_VALUE_POS.get(), MemoryStatus.VALUE_PRESENT));

    public StealBehaviour(){
        super();
        this.cooldownFor(e -> e.getRandom().nextIntBetweenInclusive(3600, 9600));
        this.startCondition(e -> e.getMood() < 0 && e.getRandom().nextInt(0, 100) > 20);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

}
