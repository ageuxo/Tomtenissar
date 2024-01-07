package io.github.ageuxo.TomteMod.block.entity;

import io.github.ageuxo.TomteMod.gui.WorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleWorkStationBlockEntity extends SimpleContainerBlockEntity {

    public SimpleWorkStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WORK_STATION.get(), pPos, pBlockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.default");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WorkStationMenu(pContainerId, pPlayerInventory, this);
    }
}
