package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.item.ItemHandlerWrapper;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Optional;

public class MilkingWorkStationBE extends SimpleWorkStationBlockEntity implements AnimalTypeWorkStation<Cow> {
    protected Int2LongArrayMap idToCooldownMap = new Int2LongArrayMap();
    protected List<Cow> cowCache;
    protected ItemHandlerWrapper wrappedHandler;
    private long lastCheck;

    public MilkingWorkStationBE(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState, StationType.MILKING);
        this.wrappedHandler = new ItemHandlerWrapper(this.itemHandler){
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        this.wrappedHandler.setInsertFilter((integer, stack) -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve().isPresent());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.milking");
    }

    @Override
    public boolean canBeWorkedAt() {
        return hasValidContainer();
    }

    public void doMilking(Cow cow){
        if (!this.level.isClientSide){
            int size = this.itemHandler.getSlots();
            FluidStack stack = new FluidStack(ForgeMod.MILK.get(), 1000);
            for (int i = 0; i < size; i++){
                if (fluidFitsInSlot(this.itemHandler, i, stack)){
                    Optional<IFluidHandlerItem> handlerItem = this.itemHandler.getStackInSlot(i).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                    if (handlerItem.isPresent()){
                        handlerItem.get().fill(stack, IFluidHandler.FluidAction.EXECUTE);
                        this.itemHandler.setStackInSlot(i, handlerItem.get().getContainer());
                        return;
                    }
                }
            }
            this.idToCooldownMap.put(cow.getId(), cow.level().getGameTime());
        }
    }

    @Override
    public List<Cow> getWorkableAnimals() {
        this.trimIdMap();
        return this.getOrFindCows();
    }

    protected boolean hasValidContainer() {
        FluidStack fluidStack = new FluidStack(ForgeMod.MILK.get(), FluidType.BUCKET_VOLUME);
        for (int i = 0; i < this.itemHandler.getSlots(); i++){
            if (fluidFitsInSlot(this.itemHandler, i, fluidStack)){
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    protected List<Cow> getOrFindCows(){
        List<Cow> foundCows;
        if (this.cowCache == null || this.level.getGameTime() - this.lastCheck  < 100){
            AABB checkBox = new AABB(this.worldPosition);
            checkBox = checkBox.inflate(8);
            foundCows = this.level.getEntities(EntityTypeTest.forClass(Cow.class), checkBox, this.type.predicate);
            this.lastCheck = this.level.getGameTime();
        } else {
            foundCows = this.cowCache;
        }
        foundCows.removeIf(this::filterByCooldown);
        this.cowCache = foundCows;
        return foundCows;
    }

    protected void trimIdMap(){
        IntArrayList ids = new IntArrayList();
        for (Int2LongMap.Entry entry : this.idToCooldownMap.int2LongEntrySet()){
            //noinspection DataFlowIssue
            if (entry.getLongValue() - this.level.getGameTime() >= 10000){
                ids.add(entry.getIntKey());
            }
        }
        for (int id : ids){
            this.idToCooldownMap.remove(id);
        }
    }

    private boolean filterByCooldown(Cow cow) {
        return this.idToCooldownMap.containsKey(cow.getId());
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
