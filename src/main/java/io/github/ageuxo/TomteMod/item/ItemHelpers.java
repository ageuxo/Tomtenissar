package io.github.ageuxo.TomteMod.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

public class ItemHelpers {
    public static boolean canStack(ItemStack held, ItemStack into){
        return into.isEmpty() || (ItemStack.isSameItemSameTags(into, held) && into.getCount() < into.getMaxStackSize());
    }

    public static int canStackHowMany(ItemStack held, ItemStack into){
        if (canStack(held, into)){
            int limit = into.isEmpty() ? 64 : into.getMaxStackSize();
            limit -= into.getCount();
            int min = Math.min(limit, held.getCount());
            return Math.max(min, 0);
        }
        return 0;
    }

    public static Pair<InteractionHand, Integer> stackAmountInHands(LivingEntity entity, ItemStack stack){
        InteractionHand hand = InteractionHand.MAIN_HAND;
        ItemStack handStack = entity.getItemInHand(hand);
        int deltaCount = ItemHelpers.canStackHowMany(stack, handStack);
        if (deltaCount <= 0) { //try other hand
            hand = InteractionHand.OFF_HAND;
            handStack = entity.getItemInHand(hand);
            deltaCount = ItemHelpers.canStackHowMany(stack, handStack);
        }
        return Pair.of(hand, deltaCount);
    }

    public static void dropHandlerItems(BlockEntity blockEntity, IItemHandler handler){
        NonNullList<ItemStack> stacks = NonNullList.withSize(handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < handler.getSlots(); i++){
            ItemStack drop = handler.getStackInSlot(i).copyAndClear();
            stacks.set(i, drop);
        }
        //noinspection DataFlowIssue
        Containers.dropContents(blockEntity.getLevel(), blockEntity.getBlockPos(), stacks);
    }
}
