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
    protected ItemStackHandler itemHandler = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            SimpleContainerBlockEntity.this.setChanged();
        }
    };

    public SimpleContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState state, int rows, int columns) {
        this(pType, pPos, state);
        this.itemHandler.setSize(rows * columns);
    }

    public SimpleContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
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
        if (this.name != null){
            pTag.putString(NAME_KEY, Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        if (pTag.contains(NAME_KEY)){
            this.name = Component.Serializer.fromJson(pTag.getString(NAME_KEY));
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
