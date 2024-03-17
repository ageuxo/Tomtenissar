package io.github.ageuxo.TomteMod.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class NameableBEMenuProvider<BE extends BlockEntity> implements MenuProvider {
    protected BlockEntity blockEntity;
    protected BlockEntityMenuConstructor<BE> menuConstructor;
    protected Level level;
    protected BlockPos pos;

    public  NameableBEMenuProvider(Level level, BlockPos pos, BlockEntityMenuConstructor<BE> menuConstructor){
        this.level = level;
        this.pos = pos;
        this.menuConstructor = menuConstructor;
    }

    @Override
    public Component getDisplayName() {
        return ((Nameable) this.level.getBlockEntity(this.pos)).getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        //noinspection unchecked
        return this.menuConstructor.createMenu(pContainerId, pPlayerInventory, (BE) this.level.getBlockEntity(this.pos));
    }
}
