package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TomteMod.MODID);

    public static final DeferredItem<DeferredSpawnEggItem> TOMTE_EGG = ITEMS.register("tomte_egg", ()->new DeferredSpawnEggItem(ModEntities.TOMTE, 0x591900, 0x153C26, new Item.Properties()));
    public static final DeferredItem<TomtePuddingItem> TOMTE_PUDDING_ITEM = ITEMS.register("tomte_pudding", ()-> new TomtePuddingItem(ModBlocks.TOMTE_PUDDING.get(), new Item.Properties()));
    public static final DeferredItem<Item> YULE_GRAIN = ITEMS.register("yule_grain", ()->new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_PUDDING = ITEMS.register("raw_pudding", ()->new Item(new Item.Properties()));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
