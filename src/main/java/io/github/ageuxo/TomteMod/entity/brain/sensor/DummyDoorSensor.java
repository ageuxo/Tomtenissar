package io.github.ageuxo.TomteMod.entity.brain.sensor;

import io.github.ageuxo.TomteMod.entity.brain.ModSensors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class DummyDoorSensor<E extends LivingEntity> extends ExtendedSensor<E> {

    public static final List<MemoryModuleType<?>> MEMORIES = List.of(
            MemoryModuleType.DOORS_TO_CLOSE
    );
    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensors.DUMMY_DOOR.get();
    }
}
