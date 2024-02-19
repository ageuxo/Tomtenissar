package io.github.ageuxo.TomteMod.block.entity.workstations;

import net.minecraft.world.entity.animal.Animal;

import java.util.List;

public interface AnimalTypeWorkStation<T extends Animal> {
    /**
     *Gets a list of animals that are valid targets according to the workstation.
     * <p>
     * Always call {@link #canBeWorkedAt()} first.
     * @return List of valid targets
     */
    List<T> getWorkableAnimals();

    /**
     * Check that all prerequisites for working at this workstation are met.
     * <p>
     * Should be called before {@link #getWorkableAnimals()}.
     *
     * @return true if this workstation can be worked at
     */
    boolean canBeWorkedAt();
}
