package io.github.ageuxo.TomteMod.item;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class CallbackItemStackHandler extends ItemStackHandler {
    protected final Runnable callback;

    public CallbackItemStackHandler(int size, Runnable callback){
        super(size);
        this.callback = callback;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.callback.run();
    }

    public static CallbackItemStackHandler blockEntityHandler(BlockEntity blockEntity, int size){
        return new CallbackItemStackHandler(size, blockEntity::setChanged);
    }
}
