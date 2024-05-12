package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.entity.workstations.AbstractAnimalWorkStation;
import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import io.github.ageuxo.TomteMod.block.entity.workstations.ShearingWorkStationBE;
import io.github.ageuxo.TomteMod.gui.BlockEntityMenuConstructor;
import io.github.ageuxo.TomteMod.gui.ShearingWorkStationMenu;
import io.github.ageuxo.TomteMod.gui.MilkingWorkStationMenu;
import io.github.ageuxo.TomteMod.item.BEWLRItem;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TomteMod.MODID);

    public static final RegistryObject<SimpleWorkStationBlock<ShearingWorkStationBE>> SHEARING_WORK_STATION = registerWorkStation("shearing", BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BELL).strength(2.5F).sound(SoundType.WOOD).noOcclusion(), ShearingWorkStationMenu::new, ShearingWorkStationBE::new, BEWLRItem.Type.SHEARING);
    public static final RegistryObject<SimpleWorkStationBlock<MilkingWorkStationBE>> MILKING_WORK_STATION = registerWorkStation("milking", BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BELL).strength(2.5F).sound(SoundType.WOOD).noOcclusion(), MilkingWorkStationMenu::new, MilkingWorkStationBE::new, BEWLRItem.Type.MILKING);

    protected static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> blockSupplier){
        RegistryObject<T> ret = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, ret);
        return ret;
    }

    protected static <T extends AbstractAnimalWorkStation<?>> RegistryObject<SimpleWorkStationBlock<T>> registerWorkStation(String baseName, BlockBehaviour.Properties properties, BlockEntityMenuConstructor<T> menuConstructor, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, BEWLRItem.Type bewlrType){
        String fullName = baseName + "_station";
        return registerBlockWithBEWLRItem(fullName, bewlrType, ()->new SimpleWorkStationBlock<>(properties, menuConstructor, blockEntitySupplier));
    }

    protected static <T extends Block> RegistryObject<T> registerBlockWithBEWLRItem(String name, BEWLRItem.Type bewlrType, Supplier<T> blockSupplier){
        RegistryObject<T> ret = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> new BEWLRItem(ret.get(), new Item.Properties(), bewlrType));
        return ret;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
