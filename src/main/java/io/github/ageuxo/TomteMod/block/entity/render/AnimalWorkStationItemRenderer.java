package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import io.github.ageuxo.TomteMod.block.entity.workstations.ShearingWorkStationBE;
import io.github.ageuxo.TomteMod.item.WorkStationItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AnimalWorkStationItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static AnimalWorkStationItemRenderer INSTANCE;
    private static final MilkingWorkStationBE MILKING_STATION = new MilkingWorkStationBE(BlockPos.ZERO, ModBlocks.MILKING_WORK_STATION.get().defaultBlockState());
    private static final ShearingWorkStationBE SHEARING_STATION = new ShearingWorkStationBE(BlockPos.ZERO, ModBlocks.SHEARING_WORK_STATION.get().defaultBlockState());
    public BlockEntityRenderDispatcher dispatcher;
    public EntityModelSet modelSet;

    public AnimalWorkStationItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.dispatcher = pBlockEntityRenderDispatcher;
        this.modelSet = pEntityModelSet;
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof WorkStationItem stationItem){
            BlockEntity blockEntity = null;
            if (stationItem.getType() == WorkStationItem.Type.MILKING){
                blockEntity = MILKING_STATION;
            } else if (stationItem.getType() == WorkStationItem.Type.SHEARING) {
                blockEntity = SHEARING_STATION;
            }
            dispatcher.renderItem(blockEntity, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
    }
}
