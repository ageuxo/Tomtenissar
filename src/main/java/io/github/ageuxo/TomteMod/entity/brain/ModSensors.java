package io.github.ageuxo.TomteMod.entity.brain;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.brain.sensor.DummyDoorSensor;
import io.github.ageuxo.TomteMod.entity.brain.sensor.NearbyBlockEntitiesSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tslat.smartbrainlib.SBLNeoForge;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(SBLNeoForge.SENSORS.getRegistryName(), TomteMod.MODID);

    public static final DeferredHolder<SensorType<?>, SensorType<NearbyBlockEntitiesSensor<?>>> NEARBY_BLOCK_ENTITIES = SENSORS.register("stealable_sensor", ()-> new SensorType<NearbyBlockEntitiesSensor<?>>(NearbyBlockEntitiesSensor::new));
    public static final DeferredHolder<SensorType<?>, SensorType<DummyDoorSensor<?>>> DUMMY_DOOR = SENSORS.register("dummy_door", ()-> new SensorType<>(DummyDoorSensor::new));

    public static void register(IEventBus bus){
        SENSORS.register(bus);
    }
}
