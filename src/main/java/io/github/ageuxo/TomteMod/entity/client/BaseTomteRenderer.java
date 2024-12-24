package io.github.ageuxo.TomteMod.entity.client;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import io.github.ageuxo.TomteMod.entity.TomteRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class BaseTomteRenderer extends MobRenderer<BaseTomte, TomteRenderState, TomteModel> {
    public BaseTomteRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TomteModel(pContext.bakeLayer(ModModelLayers.TOMTE_LAYER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public TomteRenderState createRenderState() {
        return new TomteRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(TomteRenderState renderState) {
        return TomteMod.modRL("textures/entity/tomte_elf.png");
    }
}
