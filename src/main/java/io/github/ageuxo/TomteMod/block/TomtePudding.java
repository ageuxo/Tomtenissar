package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.ModParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TomtePudding extends Block {
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 2, 12);

    public TomtePudding(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, true));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            level.destroyBlock(pos, true);
            return InteractionResult.SUCCESS;
        } else {
            player.displayClientMessage(
                    Component.translatable("info.tomtemod.pudding.eat").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                    true);
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILLED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(FILLED) && random.nextInt(15) == 0) {
            Vec3 center = pos.getCenter();
            int spread = 6;
            level.addParticle(ModParticles.STEAM.get(),
                    center.x + (random.nextInt(spread) / 10d) - (3 / 10d),
                    pos.getY() + (random.nextInt(2, 3) / 10d),
                    center.z + (random.nextInt(spread) / 10d) - (3 / 10d),
                    0,
                    0,
                    0
                    );
        }
    }

}
