package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.block.entity.SimplePresentBlockEntity;
import io.github.ageuxo.TomteMod.gui.PresentMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SimplePresentBlock extends BaseEntityBlock {
    protected SimplePresentBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SimplePresentBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider(
                (pContainerId, pPlayerInventory, pPlayer) -> new PresentMenu(pContainerId, pPlayerInventory,
                        (SimplePresentBlockEntity) pLevel.getBlockEntity(pPos), null), Component.translatable("tomtemod.gui.present.name"));
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide){
            return InteractionResult.SUCCESS;
        } else {
            if (pLevel.getBlockEntity(pPos) instanceof SimplePresentBlockEntity present){
                NetworkHooks.openScreen((ServerPlayer) pPlayer, present, pPos);
            }
        }
        return InteractionResult.CONSUME;
    }
}
