package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WrappingPaperItem extends Item {
    public WrappingPaperItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockState state = level.getBlockState(pContext.getClickedPos());
        if (state.is(Blocks.CHEST)){
            level.setBlockAndUpdate(pContext.getClickedPos(), ModBlocks.SIMPLE_PRESENT.get().withPropertiesOf(state));
            pContext.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useOn(pContext);
    }
}
