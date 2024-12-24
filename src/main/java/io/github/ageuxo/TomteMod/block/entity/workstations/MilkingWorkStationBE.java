package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.gui.MilkingWorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MilkingWorkStationBE extends AbstractAnimalWorkStation<Cow> {

    public MilkingWorkStationBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MILKING_STATION.get(), pPos, pBlockState, (animal -> animal instanceof Cow && !(animal instanceof MushroomCow)), 3, 5, 0);
        this.wrappedHandler.setInsertFilter((integer, stack) -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.milking");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MilkingWorkStationMenu(pContainerId, pPlayerInventory, (MilkingWorkStationBE) this.level.getBlockEntity(this.worldPosition));
    }

    @Override
    public boolean canBeWorkedAt() {
        return hasValidContainer();
    }

    @Override
    public ItemStack getDisplayItem() {
        return Items.MILK_BUCKET.getDefaultInstance();
    }

    public void doAction(Cow cow){
        if (!this.level.isClientSide){
            int size = this.itemHandler.getSlots();
            FluidStack stack = new FluidStack(NeoForgeMod.MILK.get(), 1000);
            for (int i = 0; i < size; i++){
                if (this.itemHandler.getStackInSlot(i).is(Items.BUCKET)){
                    ItemStack filled = FluidUtil.getFilledBucket(stack);
                    if (!filled.isEmpty()){
                        this.itemHandler.setStackInSlot(i, filled);
                        break;
                    }
                } else if (fluidFitsInSlot(this.itemHandler, i, stack)){
                    IFluidHandlerItem handlerItem = this.itemHandler.getStackInSlot(i).getCapability(Capabilities.FluidHandler.ITEM);
                    if (handlerItem != null){
                        handlerItem.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                        this.itemHandler.setStackInSlot(i, handlerItem.getContainer());
                        break;
                    }
                }
            }
            this.idToCooldownMap.put(cow.getId(), cow.level().getGameTime());
        }
    }

    @Override
    public List<Cow> getWorkableAnimals() {
        this.trimIdMap();
        return this.getOrFindAnimals(Cow.class);
    }

    protected boolean hasValidContainer() {
        FluidStack fluidStack = new FluidStack(NeoForgeMod.MILK.get(), FluidType.BUCKET_VOLUME);
        for (int i = 0; i < this.getItemHandler().getSlots(); i++){
            if (fluidFitsInSlot(this.getItemHandler(), i, fluidStack)){
                return true;
            }
        }
        return false;
    }

    protected boolean fluidFitsInSlot(IItemHandlerModifiable itemHandler, int slot, FluidStack fluidStack){
        IFluidHandlerItem fluidCap = itemHandler.getStackInSlot(slot).getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidCap != null){
            return fluidCap.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != 0;
        }
        return false;
    }

}
