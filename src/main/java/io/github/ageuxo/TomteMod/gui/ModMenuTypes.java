package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TomteMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MilkingWorkStationMenu>> WORK_STATION = registerMenuType(MilkingWorkStationMenu::new, "work_station_menu");
    public static final DeferredHolder<MenuType<?>, MenuType<ShearingWorkStationMenu>> SHEARING_STATION = registerMenuType(ShearingWorkStationMenu::new, "shearing_station_menu");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name){
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
