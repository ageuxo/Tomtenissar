package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TomteMod.MODID);

    public static final DeferredItem<SpawnEggItem> TOMTE_EGG = ITEMS.register("tomte_egg", ()->new SpawnEggItem(ModEntities.TOMTE.get(), 0x591900, 0x153C26, new Item.Properties()));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
