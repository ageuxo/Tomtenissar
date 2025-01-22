package io.github.ageuxo.TomteMod;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, TomteMod.MODID);

    public static final Supplier<SimpleParticleType> STEAM = TYPES.register(
            "steam", ()-> new SimpleParticleType(false)
    );

    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }

}
