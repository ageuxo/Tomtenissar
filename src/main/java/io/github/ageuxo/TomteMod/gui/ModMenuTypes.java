package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.TomteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, TomteMod.MODID);

    public static final RegistryObject<MenuType<PresentMenu>> PRESENT = registerMenuType(PresentMenu::new, "present_menu");
    public static final RegistryObject<MenuType<MilkingWorkStationMenu>> WORK_STATION = registerMenuType(MilkingWorkStationMenu::new, "work_station_menu");
    public static final RegistryObject<MenuType<ShearingWorkStationMenu>> SHEARING_STATION = registerMenuType(ShearingWorkStationMenu::new, "shearing_station_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name){
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
