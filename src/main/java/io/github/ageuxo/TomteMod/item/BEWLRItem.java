package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.block.entity.render.AnimalWorkStationItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class BEWLRItem extends BlockItem {
    private final Type type;
    public BEWLRItem(Block block, Properties pProperties, Type type) {
        super(block, pProperties);
        this.type = type;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return AnimalWorkStationItemRenderer.INSTANCE;
            }
        });
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        SHEARING,
        MILKING
    }




}
