package io.github.ageuxo.TomteMod.block.entity;

import io.github.ageuxo.TomteMod.gui.PresentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class SimplePresentBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    protected Component name;
    protected ItemStackHandler itemHandler = new ItemStackHandler(9);
    protected final PresentOpenersCounter openersCounter = new PresentOpenersCounter(this);
    protected float openness;

    public SimplePresentBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SIMPLE_PRESENT.get(), pPos, pBlockState);
    }

    @Override
    public Component getName() {
        return name != null ? this.name : this.getDefaultName();
    }

    private Component getDefaultName() {
        return Component.translatable("tomtemod.gui.present.name");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", itemHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putFloat("openness", this.openness);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.openness = tag.getInt("openness");
    }

    public float getOpenness() {
        return openness;
    }

    public static class PresentOpenersCounter extends ContainerOpenersCounter{
        protected SimplePresentBlockEntity parent;

        public PresentOpenersCounter(SimplePresentBlockEntity parent){
            this.parent = parent;
        }

        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            this.parent.level.playSound(null, parent.getBlockPos(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            this.parent.level.playSound(null, parent.getBlockPos(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {

        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof PresentMenu;
        }
    }
}
