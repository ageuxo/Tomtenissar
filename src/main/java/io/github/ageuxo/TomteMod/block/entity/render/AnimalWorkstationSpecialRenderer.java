package io.github.ageuxo.TomteMod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.TomteMod.item.BEWLRItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AnimalWorkstationSpecialRenderer implements SpecialModelRenderer<BEWLRItem.Type> {

    public AnimalWorkstationSpecialRenderer() {

    }

    @Override
    public void render(@Nullable BEWLRItem.Type type, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockState state = type.block().get().defaultBlockState();
        AnimalWorkStationRenderer.render(poseStack, bufferSource, packedLight, packedOverlay, null, Direction.NORTH, type.displayItem().getDefaultInstance(), state, minecraft.getBlockRenderer(), minecraft.getItemRenderer());
    }

    @Nullable
    @Override
    public BEWLRItem.Type extractArgument(ItemStack stack) {
        if (stack.getItem() instanceof BEWLRItem bewlrItem) {
            return bewlrItem.getType();
        } else {
            return BEWLRItem.Type.MILKING;
        }
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked{
        public static final MapCodec<AnimalWorkstationSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BEWLRItem.Type.CODEC.fieldOf("type").forGetter(Unbaked::stationType)
        ).apply(instance, Unbaked::new));

        private final BEWLRItem.Type stationType;

        public Unbaked(BEWLRItem.Type stationType) {
            this.stationType = stationType;
        }

        public BEWLRItem.Type stationType() {
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
