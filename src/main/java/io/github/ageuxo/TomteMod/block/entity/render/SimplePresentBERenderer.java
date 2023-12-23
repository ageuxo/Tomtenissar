package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.entity.SimplePresentBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.ModelData;

public class SimplePresentBERenderer implements BlockEntityRenderer<SimplePresentBlockEntity> {
    public static ResourceLocation PRESENT_LID = TomteMod.modRL("block/present/simple_present_lid");
    protected ModelBlockRenderer modelBlockRenderer;
    protected BakedModel lidModel;

    public SimplePresentBERenderer(BlockEntityRendererProvider.Context context){
        this.modelBlockRenderer = context.getBlockRenderDispatcher().getModelRenderer();
        this.lidModel = context.getBlockRenderDispatcher().getBlockModelShaper().getModelManager().getModel(PRESENT_LID);
    }

    @Override
    public void render(SimplePresentBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        float openness = blockEntity.getOpenness();
        this.modelBlockRenderer.renderModel(poseStack.last(), buffer.getBuffer(RenderType.cutout()),
                null, this.lidModel, 0F, 0F, 0F, packedLight, packedOverlay, ModelData.EMPTY, RenderType.cutout());
    }
}
