package io.github.ageuxo.TomteMod;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> STEALABLE_CONTAINER = BlockTags.create(TomteMod.modRL("stealable_container"));

    public static final TagKey<Item> STEALABLES = ItemTags.create(TomteMod.modRL("stealables"));
}
