package io.github.ageuxo.TomteMod.mixins;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChestRenderer.class)
public interface ChestRendererAccessor {
    @Accessor
    ModelPart getLid();

    @Mutable
    @Accessor
    void setLid(ModelPart lid);

    @Accessor
    ModelPart getBottom();

    @Mutable
    @Accessor
    void setBottom(ModelPart bottom);

    @Accessor
    ModelPart getLock();

    @Mutable
    @Accessor
    void setLock(ModelPart lock);

    @Accessor
    ModelPart getDoubleLeftLid();

    @Mutable
    @Accessor
    void setDoubleLeftLid(ModelPart doubleLeftLid);

    @Accessor
    ModelPart getDoubleLeftBottom();

    @Mutable
    @Accessor
    void setDoubleLeftBottom(ModelPart doubleLeftBottom);

    @Accessor
    ModelPart getDoubleLeftLock();

    @Mutable
    @Accessor
    void setDoubleLeftLock(ModelPart doubleLeftLock);

    @Accessor
    ModelPart getDoubleRightLid();

    @Mutable
    @Accessor
    void setDoubleRightLid(ModelPart doubleRightLid);

    @Accessor
    ModelPart getDoubleRightBottom();

    @Mutable
    @Accessor
    void setDoubleRightBottom(ModelPart doubleRightBottom);

    @Accessor
    ModelPart getDoubleRightLock();

    @Mutable
    @Accessor
    void setDoubleRightLock(ModelPart doubleRightLock);

    @Accessor
    boolean isXmasTextures();

    @Accessor
    void setXmasTextures(boolean xmasTextures);
}
