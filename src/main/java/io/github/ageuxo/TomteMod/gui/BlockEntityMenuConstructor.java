package io.github.ageuxo.TomteMod.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockEntityMenuConstructor<BE extends BlockEntity> {
    /**
     * @param id ContainerId
     * @param inventory Player's Inventory
     * @param nameableBlockEntity BlockEntity
     * @return constructedMenu
     */
    @Nullable
    AbstractContainerMenu createMenu(int id, Inventory inventory, BE nameableBlockEntity);
}
