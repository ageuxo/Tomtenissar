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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MilkingWorkStationBE extends AbstractAnimalWorkStation<Cow> {

    public MilkingWorkStationBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MILKING_STATION.get(), pPos, pBlockState, (animal -> animal instanceof Cow && !(animal instanceof MushroomCow)), 3, 5, 0);
        this.wrappedHandler.setInsertFilter((integer, stack) -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isPresent());
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
            FluidStack stack = new FluidStack(ForgeMod.MILK.get(), 1000);
            for (int i = 0; i < size; i++){
                if (this.itemHandler.getStackInSlot(i).is(Items.BUCKET)){
                    ItemStack filled = FluidUtil.getFilledBucket(stack);
                    if (!filled.isEmpty()){
                        this.itemHandler.setStackInSlot(i, filled);
                        break;
                    }
                } else if (fluidFitsInSlot(this.itemHandler, i, stack)){
                    Optional<IFluidHandlerItem> handlerItem = this.itemHandler.getStackInSlot(i).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                    if (handlerItem.isPresent()){
                        handlerItem.get().fill(stack, IFluidHandler.FluidAction.EXECUTE);
                        this.itemHandler.setStackInSlot(i, handlerItem.get().getContainer());
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
        FluidStack fluidStack = new FluidStack(ForgeMod.MILK.get(), FluidType.BUCKET_VOLUME);
        for (int i = 0; i < this.getItemHandler().getSlots(); i++){
            if (fluidFitsInSlot(this.getItemHandler(), i, fluidStack)){
                return true;
            }
        }
        return false;
    }

    protected boolean fluidFitsInSlot(IItemHandler itemHandler, int slot, FluidStack fluidStack){
        Optional<IFluidHandlerItem> fluidItemOptional = itemHandler.getStackInSlot(slot).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
        //noinspection OptionalIsPresent
        if (fluidItemOptional.isPresent()){
            return fluidItemOptional.get().fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) != 0;
        }
        return false;
    }

}
