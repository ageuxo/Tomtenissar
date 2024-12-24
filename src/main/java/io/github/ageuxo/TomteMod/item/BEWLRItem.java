package io.github.ageuxo.TomteMod.item;

import com.mojang.serialization.Codec;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.block.SimpleWorkStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BEWLRItem extends BlockItem {
    private final Type type;
    public BEWLRItem(Block block, Properties pProperties, Type type) {
        super(block, pProperties);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        SHEARING(Items.SHEARS, ModBlocks.SHEARING_WORK_STATION),
        MILKING(Items.MILK_BUCKET, ModBlocks.MILKING_WORK_STATION);

        private final Item displayItem;
        private final DeferredBlock<? extends SimpleWorkStationBlock<?>> block;

        Type(Item displayItem, DeferredBlock<? extends SimpleWorkStationBlock<?>> block) {
            this.displayItem = displayItem;
            this.block = block;
        }

        public Item displayItem() {
            return displayItem;
        }

        public DeferredBlock<? extends SimpleWorkStationBlock<?>> block() {
            return block;
        }

        public static final Codec<Type> CODEC = Codec.STRING.xmap(Type::valueOf, Type::name);
    }

}
