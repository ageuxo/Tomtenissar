package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.item.WorkStationItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AnimalWorkstationSpecialRenderer implements SpecialModelRenderer<WorkStationItem.Type> {
    public static final ResourceLocation ID = TomteMod.modRL("workstation");

    public AnimalWorkstationSpecialRenderer() { }

    @Override
    public void render(@Nullable WorkStationItem.Type type, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockState state = type.block().get().defaultBlockState();
        AnimalWorkStationRenderer.render(poseStack, bufferSource, packedLight, packedOverlay, null, Direction.NORTH, type.displayItem().getDefaultInstance(), state, minecraft.getBlockRenderer(), minecraft.getItemRenderer());
    }

    @Nullable
    @Override
    public WorkStationItem.Type extractArgument(ItemStack stack) {
        if (stack.getItem() instanceof WorkStationItem workStationItem) {
            return workStationItem.getType();
        }
        LogUtils.getLogger().error("{} is not a WorkStationItem", stack);
        return null;
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked{
        public static final MapCodec<AnimalWorkstationSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                WorkStationItem.Type.CODEC.fieldOf("station_type").forGetter(Unbaked::stationType)
        ).apply(instance, Unbaked::new));

        private final WorkStationItem.Type stationType;

        public Unbaked(WorkStationItem.Type stationType) {
            this.stationType = stationType;
        }

        public WorkStationItem.Type stationType() {
            return stationType;
        }

        @Nullable
        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new AnimalWorkstationSpecialRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
