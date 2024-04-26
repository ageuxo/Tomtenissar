package io.github.ageuxo.TomteMod.item;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

public class ItemHandlerWrapper implements IItemHandlerModifiable {

    public ItemStackHandler baseHandler;
    public BiPredicate<Integer, ItemStack> insertFilter;
    public Int2IntFunction slotLimiter = (slot) -> 64;

    public ItemHandlerWrapper(ItemStackHandler baseHandler){
        this.baseHandler = baseHandler;
    }

    @Override
    public int getSlots() {
        return baseHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return baseHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (this.insertFilter.test(slot, stack)){
            return baseHandler.insertItem(slot, stack, simulate);
        } else {
            return stack;
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return baseHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimiter.get(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.insertFilter.test(slot, stack);
    }

    public void setInsertFilter(BiPredicate<Integer, ItemStack> insertFilter) {
        this.insertFilter = insertFilter;
    }

    public void setSlotLimiter(Int2IntFunction slotLimiter) {
        this.slotLimiter = slotLimiter;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.baseHandler.setStackInSlot(slot, stack);
    }
}
