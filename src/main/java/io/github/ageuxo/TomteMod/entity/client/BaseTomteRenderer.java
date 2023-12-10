package io.github.ageuxo.TomteMod.entity.client;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.BaseTomte;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BaseTomteRenderer extends MobRenderer<BaseTomte, BaseTomteModel<BaseTomte>> {
    public BaseTomteRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BaseTomteModel<>(pContext.bakeLayer(ModModelLayers.TOMTE_LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseTomte pEntity) {
        return TomteMod.modRL("textures/entity/tomte_dev.png");
    }
}
