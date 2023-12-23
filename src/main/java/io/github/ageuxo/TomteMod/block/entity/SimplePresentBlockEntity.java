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
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class SimplePresentBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    protected Component name;
    protected ItemStackHandler itemHandler = new ItemStackHandler(9);
    protected final PresentOpenersCounter openersCounter = new PresentOpenersCounter(this);
    protected final ChestLidController lidController = new ChestLidController();
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
        return new PresentMenu(pContainerId, pPlayerInventory, this);
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

    public float getOpenness(float partialTick) {
        return lidController.getOpenness(partialTick);
    }

    @SuppressWarnings("DataFlowIssue")
    public void startOpen(Player player){
        if (!this.remove && !player.isSpectator()){
            this.openersCounter.incrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void stopOpen(Player player){
        if (!this.remove && !player.isSpectator()){
            this.openersCounter.decrementOpeners(player, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, SimplePresentBlockEntity blockEntity) {
        blockEntity.lidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1){
            this.lidController.shouldBeOpen(pType > 0);
            return true;
        } else {
            return super.triggerEvent(pId, pType);
        }
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int eventId, int eventParam){
        level.blockEvent(pos, state.getBlock(), eventId, eventParam);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @SuppressWarnings("DataFlowIssue")
    public void recheckOpen(){
        if (!this.remove){
            this.openersCounter.recheckOpeners(this.level, this.getBlockPos(), this.getBlockState());
        }
    }



    public static class PresentOpenersCounter extends ContainerOpenersCounter{

        protected SimplePresentBlockEntity parent;

        public PresentOpenersCounter(SimplePresentBlockEntity parent){
            this.parent = parent;
        }

        @SuppressWarnings("DataFlowIssue")
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            this.parent.level.playSound(null, parent.getBlockPos(), SoundEvents.CHEST_OPEN, SoundSource.BLOCKS);
        }

        @SuppressWarnings("DataFlowIssue")
        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            this.parent.level.playSound(null, parent.getBlockPos(), SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
            this.parent.signalOpenCount(pLevel, pPos, pState, pCount, pOpenCount);
        }
        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof PresentMenu;
        }

    }
}
