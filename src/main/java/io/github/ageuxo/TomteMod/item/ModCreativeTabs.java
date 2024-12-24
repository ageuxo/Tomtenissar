package io.github.ageuxo.TomteMod.item;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.stream.Collectors;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TomteMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOMTE = TABS.register("tomte",
            ()-> CreativeModeTab.builder()
                    .icon(ModBlocks.MILKING_WORK_STATION::toStack)
                    .title(Component.literal("Tomte Mod"))
                    .displayItems((pParameters, pOutput) -> {
                        Set<ItemStack> items = ModItems.ITEMS.getEntries().stream()
                                .map(DeferredHolder::get)
                                .map(Item::getDefaultInstance)
                                .collect(Collectors.toUnmodifiableSet());
                        pOutput.acceptAll(items);
                    }).build());

    public static void register(IEventBus bus){
        TABS.register(bus);
    }
}
