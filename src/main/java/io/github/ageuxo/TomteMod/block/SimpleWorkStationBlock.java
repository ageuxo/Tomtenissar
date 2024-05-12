package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.block.entity.workstations.AbstractAnimalWorkStation;
import io.github.ageuxo.TomteMod.gui.BlockEntityMenuConstructor;
import io.github.ageuxo.TomteMod.gui.NameableBEMenuProvider;
import io.github.ageuxo.TomteMod.item.ItemHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SimpleWorkStationBlock<S extends AbstractAnimalWorkStation<?>> extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Block.box(2.5, 0, 2.5, 12.5, 10, 12.5);
    public BlockEntityMenuConstructor<S> menuConstructor;
    public BlockEntityType.BlockEntitySupplier<S> blockEntitySupplier;

    protected SimpleWorkStationBlock(Properties pProperties, BlockEntityMenuConstructor<S> menuConstructor, BlockEntityType.BlockEntitySupplier<S> blockEntitySupplier) {
        super(pProperties);
        this.menuConstructor = menuConstructor;
        this.blockEntitySupplier = blockEntitySupplier;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntitySupplier.create(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.is(pNewState.getBlock())){
            if (pLevel.getBlockEntity(pPos) instanceof AbstractAnimalWorkStation<?> workStation){
                ItemHelpers.dropHandlerItems(workStation, workStation.getItemHandler());
                workStation.setRemoved();
            }
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new NameableBEMenuProvider<>(pLevel, pPos, menuConstructor);
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide){
            return InteractionResult.SUCCESS;
        } else {
            if (pLevel.getBlockEntity(pPos) instanceof AbstractAnimalWorkStation<?> workStation){
                NetworkHooks.openScreen((ServerPlayer) pPlayer, workStation, byteBuf -> byteBuf.writeBlockPos(pPos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
