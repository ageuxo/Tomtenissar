package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.block.entity.SimpleContainerBlockEntity;
import io.github.ageuxo.TomteMod.gui.BlockEntityMenuConstructor;
import io.github.ageuxo.TomteMod.gui.ShearingWorkStationMenu;
import io.github.ageuxo.TomteMod.gui.WorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class SimpleWorkStationBlockEntity extends SimpleContainerBlockEntity {
    public final StationType type;
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public SimpleWorkStationBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, StationType stationType){
        super(blockEntityType, pos, state, stationType.rows, stationType.columns);
        this.type = stationType;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyItemHandler = LazyOptional.of(()-> itemHandler);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public enum StationType {
        MILKING(3, 5, (animal -> animal instanceof Cow && !(animal instanceof MushroomCow)), MilkingWorkStationBE::new, Cow.class, WorkStationMenu::new),
        SHEARING(3, 5, animal -> {
            if (animal instanceof Sheep sheep) return sheep.readyForShearing();
            return false;
        }, ShearingWorkStationBE::new, Sheep.class, ((id, inventory, blockEntity) -> new  ShearingWorkStationMenu(id, inventory, (ShearingWorkStationBE) blockEntity)));

        public final Predicate<? super Animal> predicate;
        public final int rows;
        public final int columns;
        public final BlockEntitySupplier<SimpleWorkStationBlockEntity> blockEntitySupplier;
        public final Class<? extends Animal> clazz;
        public final BlockEntityMenuConstructor<SimpleWorkStationBlockEntity> menu;

        StationType(int rows, int columns, @Nullable Predicate<? super Animal> predicate, BlockEntitySupplier<SimpleWorkStationBlockEntity> blockEntitySupplier, Class<? extends Animal> clazz, BlockEntityMenuConstructor<SimpleWorkStationBlockEntity> menu){
            this.predicate = predicate;
            this.rows = rows;
            this.columns = columns;
            this.blockEntitySupplier = blockEntitySupplier;
            this.clazz = clazz;
            this.menu = menu;
        }
    }
}
