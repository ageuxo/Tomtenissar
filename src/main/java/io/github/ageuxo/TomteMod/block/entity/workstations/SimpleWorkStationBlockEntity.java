package io.github.ageuxo.TomteMod.block.entity.workstations;

import io.github.ageuxo.TomteMod.block.entity.ModBlockEntities;
import io.github.ageuxo.TomteMod.block.entity.SimpleContainerBlockEntity;
import io.github.ageuxo.TomteMod.gui.WorkStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SimpleWorkStationBlockEntity extends SimpleContainerBlockEntity {
    public final StationType type;

    public SimpleWorkStationBlockEntity(BlockPos pPos, BlockState pBlockState, StationType type) {
        super(ModBlockEntities.MILKING_STATION.get(), pPos, pBlockState, type.rows, type.columns);
        this.type = type;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("tomtemod.gui.workstation.default");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WorkStationMenu(pContainerId, pPlayerInventory, this);
    }

    public enum StationType {

        MILKING(3, 5, (animal -> animal instanceof Cow && !(animal instanceof MushroomCow))),
        SHEARING(3, 5, animal -> {
            if (animal instanceof Sheep sheep) return sheep.readyForShearing();
            return false;
        });
        public final Predicate<? super Animal> predicate;
        public final int rows;
        public final int columns;

        StationType(int rows, int columns, Predicate<? super Animal> predicate){
            this.predicate = predicate;
            this.rows = rows;
            this.columns = columns;
        }
    }
}
