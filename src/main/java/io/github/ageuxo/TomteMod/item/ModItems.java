package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TomteMod.MODID);

    public static final DeferredItem<Item> TOMTE_EGG = ITEMS.register("tomte_egg", ()->new SpawnEggItem(ModEntities.TOMTE.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, TomteMod.modRL("tomte_egg")))));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
