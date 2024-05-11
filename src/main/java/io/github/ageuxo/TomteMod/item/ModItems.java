package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TomteMod.MODID);

    public static final RegistryObject<Item> TOMTE_EGG = ITEMS.register("tomte_egg", ()->new ForgeSpawnEggItem(ModEntities.TOMTE, 0x591900, 0x153C26, new Item.Properties()));

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
