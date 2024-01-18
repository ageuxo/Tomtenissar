package io.github.ageuxo.TomteMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final TagKey<Block> TOMTE_NOTEWORTHY = BlockTags.create(TomteMod.modRL("tomte_noteworthy"));

    public static final TagKey<Item> STEALABLES = ItemTags.create(TomteMod.modRL("stealables"));

    public static final TagKey<PoiType> WORK_STATIONS = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, TomteMod.modRL("work_stations"));
}
