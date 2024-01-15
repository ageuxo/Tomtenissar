package io.github.ageuxo.TomteMod.block.entity.workstations;

import net.minecraft.world.entity.animal.Animal;

import java.util.List;

public interface AnimalTypeWorkStation<T extends Animal> {
    List<T> getWorkableAnimals();
}
