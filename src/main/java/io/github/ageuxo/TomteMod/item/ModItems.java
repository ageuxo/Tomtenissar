package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TomteMod.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
