package io.github.ageuxo.TomteMod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleContainerBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    public static final String NAME_KEY = "CustomName";
    protected Component name;
    protected ItemStackHandler itemHandler;

    public SimpleContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.itemHandler = new ItemStackHandler();
    }

    @Override
    public Component getName() {
        return name != null ? this.name : this.getDefaultName();
    }

    protected abstract Component getDefaultName();

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer);

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemHandler.serializeNBT());
        if (pTag.contains(NAME_KEY)){
            this.name = Component.Serializer.fromJson(pTag.getString(NAME_KEY));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag);
        if (this.name != null){
            pTag.putString(NAME_KEY, Component.Serializer.toJson(this.name));
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
