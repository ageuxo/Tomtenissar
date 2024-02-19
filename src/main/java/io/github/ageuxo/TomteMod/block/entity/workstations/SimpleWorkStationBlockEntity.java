package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.block.entity.SimpleContainerBlockEntity;
import io.github.ageuxo.TomteMod.gui.WorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SimpleWorkStationBlockEntity extends SimpleContainerBlockEntity {
    public final StationType type;
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public SimpleWorkStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState, StationType.BASE);
    }

    public SimpleWorkStationBlockEntity(BlockPos pPos, BlockState pBlockState, StationType type) {
        super(ModBlockEntities.MILKING_STATION.get(), pPos, pBlockState, type.rows, type.columns);
        this.type = type;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyItemHandler = LazyOptional.of(()-> itemHandler);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.default");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WorkStationMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public enum StationType {
        BASE(3, 5, null, SimpleWorkStationBlockEntity::new),
        MILKING(3, 5, (animal -> animal instanceof Cow && !(animal instanceof MushroomCow)), MilkingWorkStationBE::new),
        SHEARING(3, 5, animal -> {
            if (animal instanceof Sheep sheep) return sheep.readyForShearing();
            return false;
        }, null);//TODO implement shearing

        public final Predicate<? super Animal> predicate;
        public final int rows;
        public final int columns;
        public final BlockEntitySupplier<SimpleWorkStationBlockEntity> blockEntitySupplier;

        StationType(int rows, int columns, @Nullable Predicate<? super Animal> predicate, BlockEntitySupplier<SimpleWorkStationBlockEntity> blockEntitySupplier){
            this.predicate = predicate;
            this.rows = rows;
            this.columns = columns;
            this.blockEntitySupplier = blockEntitySupplier;
        }
    }
}
