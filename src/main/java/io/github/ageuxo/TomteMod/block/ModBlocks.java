package io.github.ageuxo.TomteMod.block;

import io.github.ageuxo.TomteMod.TomteMod;
import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TomteMod.MODID);

    public static final RegistryObject<Block> PRESENT = registerBlockWithItem("present", ()->new ChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), ModBlockEntities.PRESENT_TYPE)); //TODO BlockItem


    protected static <T extends Block> RegistryObject<T> registerBlockWithItem(String name, Supplier<T> blockSupplier){
        RegistryObject<T> ret = BLOCKS.register(name, blockSupplier);
        registerBlockItem(name, ret);
        return ret;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
