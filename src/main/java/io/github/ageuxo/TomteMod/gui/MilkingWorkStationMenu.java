package io.github.ageuxo.TomteMod.gui;

import io.github.ageuxo.TomteMod.block.entity.workstations.MilkingWorkStationBE;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MilkingWorkStationMenu extends SimpleContainerMenu<MilkingWorkStationBE> {

    public MilkingWorkStationMenu(int pContainerId, Inventory inv, MilkingWorkStationBE blockEntity) {
        super(ModMenuTypes.WORK_STATION.get(), pContainerId, inv, blockEntity, 3, 5);
    }

    public MilkingWorkStationMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, (MilkingWorkStationBE) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
}
