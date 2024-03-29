package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.block.entity.workstations.SimpleWorkStationBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class WorkStationMenu extends SimpleContainerMenu<SimpleWorkStationBlockEntity> {

    public WorkStationMenu(int pContainerId, Inventory inv, SimpleWorkStationBlockEntity blockEntity) {
        super(ModMenuTypes.WORK_STATION.get(), pContainerId, inv, blockEntity, blockEntity.type.rows, blockEntity.type.columns);
    }

    public WorkStationMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, (SimpleWorkStationBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
}
