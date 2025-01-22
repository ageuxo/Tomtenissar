package io.github.ageuxo.TomteMod.datagen;

import io.github.ageuxo.TomteMod.ModParticles;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

import static io.github.ageuxo.TomteMod.TomteMod.modRL;

public class ModParticleDescriptionProvider extends ParticleDescriptionProvider {

    public ModParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(ModParticles.STEAM.get(),
                modRL("steam"),
                6,
                false);
    }
}
