package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.TomteMod.block.SimpleWorkStationBlock;
import io.github.ageuxo.TomteMod.block.entity.workstations.AbstractAnimalWorkStation;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AnimalWorkStationRenderer<T extends AbstractAnimalWorkStation<?>> implements BlockEntityRenderer<T> {
    public ItemRenderer itemRenderer;
    public BlockRenderDispatcher blockRenderDispatcher;
    public ModelBlockRenderer modelBlockRenderer;

    public AnimalWorkStationRenderer(BlockEntityRendererProvider.Context context){
        this.itemRenderer = context.getItemRenderer();
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
        this.modelBlockRenderer = new ModelBlockRenderer(new BlockColors());
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Level level = pBlockEntity.getLevel();
        render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, level, pBlockEntity.getBlockState().getValue(SimpleWorkStationBlock.FACING), pBlockEntity.getDisplayItem(), pBlockEntity.getBlockState(), this.blockRenderDispatcher, this.itemRenderer);
    }

    public static void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, @Nullable Level level, Direction facing, ItemStack displayItem, BlockState blockState, BlockRenderDispatcher blockRenderer, ItemRenderer itemRenderer) {
        pPoseStack.pushPose();
        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(), pBuffer.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS)), blockState, blockRenderer.getBlockModel(blockState), 1f, 1f, 1f, pPackedLight, pPackedOverlay);
        pPoseStack.translate(0.5, 0.65, 0.5);
        pPoseStack.rotateAround(facing.getRotation().rotateLocalY(0.3f), 0f, 0f, 0f);
        pPoseStack.scale(0.8f, 0.8f, 0.8f);
        itemRenderer.renderStatic(displayItem, ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, level, 1);
        pPoseStack.popPose();
    }
}
