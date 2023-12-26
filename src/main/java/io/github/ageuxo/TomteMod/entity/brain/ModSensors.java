package io.github.ageuxo.TomteMod.entity.brain;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.brain.sensor.DummyDoorSensor;
import io.github.ageuxo.TomteMod.entity.brain.sensor.NearbyBlockEntitiesSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.SBLForge;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(SBLForge.SENSORS.getRegistryName(), TomteMod.MODID);

    public static final RegistryObject<SensorType<NearbyBlockEntitiesSensor<?>>> NEARBY_BLOCK_ENTITIES = SENSORS.register("stealable_sensor", ()-> new SensorType<NearbyBlockEntitiesSensor<?>>(NearbyBlockEntitiesSensor::new));
    public static final RegistryObject<SensorType<DummyDoorSensor<?>>> DUMMY_DOOR = SENSORS.register("dummy_door", ()-> new SensorType<>(DummyDoorSensor::new));

    public static void register(IEventBus bus){
        SENSORS.register(bus);
    }
}
