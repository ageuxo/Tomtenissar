package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Collectors;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TomteMod.MODID);

    @SuppressWarnings("deprecation")
    public static final RegistryObject<CreativeModeTab> TOMTE = TABS.register("tomte",
            ()-> CreativeModeTab.builder()
                    .icon(()-> (BuiltInRegistries.ITEM.get(TomteMod.modRL("milking_station"))).getDefaultInstance())
                    .title(Component.literal("Tomte Mod"))
                    .displayItems((pParameters, pOutput) -> {
                        Set<ItemStack> items = ModItems.ITEMS.getEntries().stream()
                                .map(RegistryObject::get)
                                .map(Item::getDefaultInstance)
                                .collect(Collectors.toUnmodifiableSet());
                        pOutput.acceptAll(items);
                    }).build());

    public static void register(IEventBus bus){
        TABS.register(bus);
    }
}
