package io.github.ageuxo.TomteMod.item;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

public class ItemHandlerWrapper implements IItemHandler {

    public IItemHandlerModifiable baseHandler;
    public BiPredicate<Integer, ItemStack> insertFilter;
    public Int2IntFunction slotLimiter = (i) -> 64;

    public ItemHandlerWrapper(IItemHandlerModifiable baseHandler){
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
}
