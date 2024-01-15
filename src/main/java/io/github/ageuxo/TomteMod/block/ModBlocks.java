package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.entity.workstations.SimpleWorkStationBlockEntity.StationType;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TomteMod.MODID);

    public static final RegistryObject<Block> SIMPLE_PRESENT = registerBlockWithItem("simple_present", ()->new SimplePresentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<SimpleWorkStationBlock> SHEARING_WORK_STATION = registerWorkStation(StationType.SHEARING, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BELL).strength(2.5F).sound(SoundType.WOOD).noOcclusion());
    public static final RegistryObject<SimpleWorkStationBlock> MILKING_WORK_STATION = registerWorkStation(StationType.MILKING, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BELL).strength(2.5F).sound(SoundType.WOOD).noOcclusion());

    protected static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> blockSupplier){
        RegistryObject<T> ret = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, ret);
        return ret;
    }

    protected static RegistryObject<SimpleWorkStationBlock> registerWorkStation(StationType type, BlockBehaviour.Properties properties){
        String fullName = type.toString().toLowerCase(Locale.ROOT) + "_station";
        return registerBlockWithItem(fullName, ()->new SimpleWorkStationBlock(properties, type));
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
