package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.SimpleWorkStationBlock;
import io.github.ageuxo.TomteMod.block.entity.workstations.AbstractAnimalWorkStation;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3d;

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
        pPoseStack.pushPose();
        BakedModel bakedModel = blockRenderDispatcher.getBlockModel(pBlockEntity.getBlockState());
        this.modelBlockRenderer.renderModel(pPoseStack.last(), pBuffer.getBuffer(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS)), pBlockEntity.getBlockState(), bakedModel, 1f, 1f, 1f, pPackedLight, pPackedOverlay);

        pPoseStack.translate(0.5, 0.65, 0.5);
        Direction dir = pBlockEntity.getBlockState().getValue(SimpleWorkStationBlock.FACING);
        pPoseStack.rotateAround(dir.getRotation().rotateLocalY(0.3f), 0f, 0f, 0f);
        pPoseStack.scale(0.8f, 0.8f, 0.8f);
        this.itemRenderer.renderStatic(pBlockEntity.getDisplayItem(), ItemDisplayContext.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBuffer, level, 1);
        pPoseStack.popPose();
    }
}
